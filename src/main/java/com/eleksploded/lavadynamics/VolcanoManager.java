package com.eleksploded.lavadynamics;

import java.util.Random;

import com.eleksploded.lavadynamics.cap.CheckedCap;
import com.eleksploded.lavadynamics.cap.IChecked;
import com.eleksploded.lavadynamics.generator.ConeVolcanoGen;
import com.eleksploded.lavadynamics.generator.IVolcanoGenerator;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= "lavadynamics")
public class VolcanoManager {
	static Random rand = new Random();
	//@SubscribeEvent
	public static void chunkGen(ChunkEvent.Load e) {
		Chunk chunk = ((Chunk)e.getChunk());
		IChecked checked = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));

		if(!e.getWorld().isRemote()) {
			if(!checked.isChecked()) {
				checked.check();
				if(LavaDynamics.LavaConfig.getBool("tile_protect") && chunk.getTileEntitiesPos().isEmpty()) {
					if(!Utils.isVolcanoInRange(chunk)) {
						if(LavaDynamics.LavaConfig.getInt("chance") > rand.nextInt(1000) + 1) {
							spawnVolcano(chunk.getWorld(), chunk);
						}
					}
				}
			}
		}
	}
	
	static boolean active = false;
	public static void spawnVolcano(World world, Chunk chunk) {
		//----------Setup----------// 
		//if(active) { return; }
		if(world.isRemote) { return; }
		//active = true;
		boolean debug = LavaDynamics.LavaConfig.getBool("debug");
		Random rand = world.getRandom();
		//Get the center of the chunk
		int x = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int z = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();		
		//Get random y level based on LavaConfig value
		int y = LavaDynamics.LavaConfig.getInt("volcanoYLevel") + (rand.nextInt(5) - 2);
		//Get center of chunk into BlockPos
		BlockPos center = new BlockPos(x,y,z);

		if(debug) {
			LavaDynamics.Logger.info("Center Location is " + center);
		}

		//----------Lava Lake----------//

		//Generate new Lava lake at chunk center
		Utils.lake(world, Blocks.LAVA, center, rand);
		
		//lakes.
		//world.getChunkProvider().ge
		if(debug) {
			LavaDynamics.Logger.info("Lava lake generated");
		}

		//----------Lava Pillar---------//

		if(debug) {
			LavaDynamics.Logger.info("Generating Lava Pillar");
		}
		
		//Get hight and set pillar
		int height = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x, z) - center.getY();
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
			world.setBlockState(new BlockPos(x,j,z), Blocks.LAVA.getDefaultState());
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
		IVolcanoGenerator gen = getGenerator(world, new BlockPos(x, topY, z));
		if(debug) LavaDynamics.Logger.debug("Running Generator");
		gen.generate(world, rand, new BlockPos(x, topY, z));
		
		if(debug) {
			LavaDynamics.Logger.info("Done Generating. Filling with " + Blocks.LAVA);
		}
		//Fill the "Volcano" with lava
		BlockPos fill = new BlockPos(x,topY,z);
		int top = 0;
		while(world.getBlockState(fill).getBlock() != Blocks.LAVA || world.getBlockState(fill).getBlock() != Blocks.AIR || world.getBlockState(fill).getBlock() != Blocks.LAVA) {
			if(debug) {
				LavaDynamics.Logger.info("Block at " + fill + " is " + world.getBlockState(fill).getBlock());
				LavaDynamics.Logger.info("Setting " + fill + " to lava");
			}
			world.setBlockState(fill, Blocks.LAVA.getDefaultState());
			fill = fill.up();
			//Check for air
			if(world.getBlockState(fill).getBlock() == Blocks.AIR){ break; }
			if(world.getBlockState(fill).getBlock() == Blocks.WATER){ break; }
			//Cap at world limits, hopefully this should never happen though
			if(fill.getY() >= 255 || fill.getY() <= 3) {
				break;
			}
			top = fill.getY();
		}
		
		IChecked ch = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));
		
		ch.setVolcano(top);

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
		world.createExplosion(null, fill.getX(), fill.getY()+3, fill.getZ(), LavaDynamics.LavaConfig.getInt("craterSize"), LavaDynamics.LavaConfig.getBool("initialFire"), Explosion.Mode.DESTROY);

		if(debug) {
			LavaDynamics.Logger.info("Done with crater");
		}
		active = false;
		//----------Done?----------//
	}
	
	public static IVolcanoGenerator getGenerator(World world, BlockPos pos) {
		Biome biome = world.getBiome(pos);
		
		if(biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN || biome == Biomes.RIVER || biome == Biomes.FROZEN_RIVER) {
			//return new WaterVolcanoGen();
		}
		
		return new ConeVolcanoGen();
	}
}
