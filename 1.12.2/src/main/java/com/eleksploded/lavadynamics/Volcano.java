package com.eleksploded.lavadynamics;

import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import com.eleksploded.lavadynamics.generators.ConeVolcanoGen;
import com.eleksploded.lavadynamics.generators.MountianVolcanoGen;
import com.eleksploded.lavadynamics.generators.WaterVolcanoGen;
import com.eleksploded.lavadynamics.storage.StorageManager;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Volcano {
	private static boolean active = false;

	private static boolean water = false;
	private static boolean worldLoaded = false;
	static int timer = LavaConfig.volcano.volcanoCooldown;

	//World Loading events
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public static void WorldLoaded(WorldEvent.Load event) {
		worldLoaded = true;
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void WorldUnloaded(WorldEvent.Unload event) {
		worldLoaded = false;
	}

	@SubscribeEvent
	public static void OnChunkLoad(ChunkEvent.Load event) {
		
		if(!ArrayUtils.contains(LavaConfig.volcano.validDimensions, event.getWorld().provider.getDimension())){ return; }
		
		if(event.getWorld().isRemote) {
			return;
		}
		
		if(event.getWorld().playerEntities.size() == 0){
			return;
		}
		
		if(LavaConfig.general.genVolcanoDebug){
			LavaDynamics.Logger.debug(timer);
		}
		
		if(timer != 0){
			timer = timer-1;
			return;
		}
		
		if(LavaConfig.volcano.protectChunks){
			if(hasTileEntity(event.getWorld(),event.getChunk())){
				return;
			}
		}
		
		//Get Chunk that was loaded
		Chunk chunk = event.getChunk();
		//Check if chunk is tested already
		if(LavaConfig.general.genVolcanoDebug) {
			LavaDynamics.Logger.info("Checking chunk at " + chunk.x + " " + chunk.z);
		}
		
		//Get Chunk Center
		int x = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int z = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();	
		int y = chunk.getHeight(new BlockPos(x,70,z));
		
		//Check if the chunk is already tested
		if(canSpawn(chunk)){
			if(LavaConfig.general.genVolcanoDebug) {
				LavaDynamics.Logger.info("Chunk at " + chunk.x + " " + chunk.z + " is not checked already");
			}
			Random rand = new Random();
			//Random Chance to spawn a Volcano
			int num = rand.nextInt(100) + 1;
			if(num <= LavaConfig.volcano.volcanoChance) {
				//Spawn a volcano
				if(LavaConfig.general.genVolcanoDebug) {
					LavaDynamics.Logger.info("VOLCANO!!!");
				}
				//Add chunk to tested Chunks
				StorageManager.getCheckedStorage(event.getWorld().provider.getDimension()).addChecked(chunk); 
				if(!StorageManager.getVolcanoStorage(event.getWorld().provider.getDimension()).isVolcanoInRange(chunk)){
					event.getWorld().setBlockState(new BlockPos(x,y,z), LavaDynamics.VolcanoBlock.getDefaultState());
				}
			} else {
				//Add chunk to tested Chunks
				StorageManager.getCheckedStorage(event.getWorld().provider.getDimension()).addChecked(chunk);
			}
		} else {
			if(LavaConfig.general.genVolcanoDebug) {
				LavaDynamics.Logger.info("Chunk " + chunk.x + " " + chunk.z + " has already been checked");
			}
		}
		timer = LavaConfig.volcano.volcanoCooldown;
	}

	public static void genVolcano(Chunk chunk, World world) {
		try {
			StorageManager.getCheckedStorage(world.provider.getDimension()).addChecked(chunk);
		} catch (NullPointerException e) {
			LavaDynamics.Logger.error("Must be running via command, or generation is taking place in an invalid dimension. "
					+ "If it is an invalid dimension, please report on the github page.");
		}
		//----------Setup----------// 
		if(active) { return; }
		if(world.isRemote) { return; }
		active = true;
		boolean debug = LavaConfig.general.genVolcanoDebug;
		Random rand = new Random(world.getSeed());
		//Get the center of the chunk
		int x = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int z = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();		
		//Get random y level based on LavaConfig value
		int y = LavaConfig.volcano.volcanoYLevel + (rand.nextInt(5) - 2);
		//Get center of chunk into BlockPos
		BlockPos center = new BlockPos(x,y,z);

		if(debug) {
			LavaDynamics.Logger.info("Center Location is " + center);
		}

		//----------Lava Lake----------//

		//Generate new Lava lake at chunk center
		WorldGenLakes lakes = new WorldGenLakes(Blocks.LAVA);
		lakes.generate(world, rand, center);
		if(debug) {
			LavaDynamics.Logger.info("Lava lake generated");
		}

		//----------Lava Pillar---------//

		if(debug) {
			LavaDynamics.Logger.info("Generating Lava Pillar");
		}

		//Get hight and set pillar
		int height = chunk.getHeight(new BlockPos(x,y+5,z)) - center.getY();
		if(height <= 0){
			height = 1;
		}
		if(debug){
			LavaDynamics.Logger.info("Height for Generation is: " + height);
		}

		//Save top value for later. Init with random value
		int topY = 70;

		for(int i = 0; i <= height; i++) {
			//Add starting height in. Cast int cause it was treating them as Strings for some reason
			int j = (int)i + (int)center.getY();
			if(debug) {
				LavaDynamics.Logger.info("Placing Lava at y=" + j);
			}
			//Set Block, which causes block updates to smelt things
			world.setBlockState(new BlockPos(x,j,z), Blocks.MAGMA.getDefaultState());
			//Save TopY
			topY = j;
		}
		if(debug) {
			LavaDynamics.Logger.info("Lava Pillar Done");
		}

		//----------Surface Generation-----------//
		if(debug) {
			LavaDynamics.Logger.info("Generate Volcano");
		}
		
		//Custom WorldGenerator
		WorldGenerator gen = getGenerator(world, new BlockPos(x, 255, z));
		//ConeVolcanoGen gen = new ConeVolcanoGen();
		gen.generate(world, rand, new BlockPos(x, 255, z));
		if(debug) {
			LavaDynamics.Logger.info("Done Generating. Filling with " + Blocks.LAVA);
		}
		//Fill the "Volcano" with lava
		BlockPos fill = new BlockPos(x,topY,z);
		while(world.getBlockState(fill).getBlock() != Blocks.LAVA || world.getBlockState(fill).getBlock() != Blocks.AIR || world.getBlockState(fill).getBlock() != Blocks.MAGMA) {
			if(debug) {
				LavaDynamics.Logger.info("Block at " + fill + " is " + world.getBlockState(fill).getBlock());
				LavaDynamics.Logger.info("Setting " + fill + " to lava");
			}
			world.setBlockState(fill, Blocks.LAVA.getDefaultState());
			fill = fill.up();
			//Check for air
			if(world.getBlockState(fill).getBlock() == Blocks.AIR){ break; }
			if(world.getBlockState(fill).getBlock() == Blocks.WATER && water){ break; }
			//Cap at world limits, hopefully this should never happen though
			if(fill.getY() >= 255 || fill.getY() <= 3) {
				break;
			}
			try {
				StorageManager.getVolcanoStorage(world.provider.getDimension()).addVolcano(chunk, fill.getY());
			} catch (NullPointerException e) {
				LavaDynamics.Logger.error("Must be running via command, or generation is taking place in an invalid dimension. "
						+ "If it is an invalid dimension, please report on the github page.");
			}
		}
		
		if(debug) {
			LavaDynamics.Logger.info("Done filling, Spawning crater");
		}
		
		BlockPos fill1 = fill.down(4);
		
		for(int radius = 3;radius != 0;radius--){
			int x1 = fill1.getX();
			int y1 = fill1.getY()-1;
			int z1 = fill1.getZ();
			
			for(float i1 = 0; i1 < radius; i1 += 0.5) {
				for(float j1 = 0; j1 < 2 * Math.PI * i1; j1 += 0.5)
					world.setBlockState(new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1 + radius, (int)Math.floor(z1 + Math.cos(j1) * i1)), Blocks.LAVA.getDefaultState());
			}
			fill1 = fill1.down();
		}


		//Make our "eruption"
		world.newExplosion(null, fill.getX(), fill.getY()+3, fill.getZ(), LavaConfig.volcano.craterSize, LavaConfig.volcano.initialFire, true);

		if(debug) {
			LavaDynamics.Logger.info("Done with crater");
		}
		active = false;
		//----------Done?----------//
	}
	
	public static WorldGenerator getGenerator(World world, BlockPos pos) {
		Biome biome = world.getBiome(pos);
		
		if(biome == Biomes.EXTREME_HILLS || biome == Biomes.EXTREME_HILLS_EDGE || biome == Biomes.EXTREME_HILLS_WITH_TREES || biome == Biomes.MUTATED_EXTREME_HILLS || biome == Biomes.MUTATED_EXTREME_HILLS_WITH_TREES || biome == Biomes.SAVANNA_PLATEAU) {
			return new MountianVolcanoGen();
		}
		
		if(biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN || biome == Biomes.RIVER || biome == Biomes.FROZEN_RIVER) {
			water = true;
			return new WaterVolcanoGen();
		}
		
		return new ConeVolcanoGen();
	}
	
	public static boolean hasTileEntity(World world, Chunk chunk){
		for(BlockPos pos : chunk.getTileEntityMap().keySet()){
			if(world.getChunk(pos).equals(chunk)){
				return true;
			}
		}
		return false;
	}
	
	static boolean canSpawn(Chunk chunk){
		if(LavaConfig.volcano.disaster){
			return worldLoaded;
		} else {
			if(worldLoaded && !StorageManager.getCheckedStorage(chunk.getWorld().provider.getDimension()).isChecked(chunk) && !LavaConfig.volcano.worldGen) {
				return true;
			} else {
				return false;
			}
		}
	}
}