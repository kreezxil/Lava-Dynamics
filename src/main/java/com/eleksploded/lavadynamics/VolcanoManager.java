package com.eleksploded.lavadynamics;

import java.util.Random;

import com.eleksploded.lavadynamics.cap.CheckedCap;
import com.eleksploded.lavadynamics.cap.IChecked;
import com.eleksploded.lavadynamics.generator.ConeVolcanoGen;
import com.eleksploded.lavadynamics.generator.IVolcanoGenerator;
import com.eleksploded.lavadynamics.utils.Utils;
import com.eleksploded.lavadynamics.utils.VolcanoCache;

import net.minecraft.block.Blocks;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= "lavadynamics")
public class VolcanoManager {
	static Random rand = new Random();
	private static boolean worldLoaded;
	@SubscribeEvent
	public static void chunkGen(ChunkEvent.Load e) {
		if(!e.getWorld().isRemote()) {

			if(!worldLoaded) return;

			boolean debug = LavaDynamics.LavaConfig.getBool("debug");
			Chunk chunk = ((Chunk)e.getChunk());
			IChecked checked = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));

			if (debug) LavaDynamics.Logger.debug("Checking Chunk: " + chunk.getPos().x + "|" + chunk.getPos().z);

			if(VolcanoCache.isCachedVolcano(chunk)) return;

			if(!checked.isChecked()) {
				checked.check();
				if (debug) LavaDynamics.Logger.debug("Checked Chunk: " + chunk.getPos().x + "|" + chunk.getPos().z);
				if(!LavaDynamics.LavaConfig.getBool("tile_protect") || chunk.getTileEntitiesPos().isEmpty()) {
					if (debug) LavaDynamics.Logger.debug("Tile Check Passed");

					chunk.getWorld().getServer().enqueue(new TickDelayedTask(10, () -> {
						if(!Utils.isVolcanoInRange((ServerWorld)e.getWorld(), chunk)) {
							if (debug) LavaDynamics.Logger.debug("No Volcano In Range");
							if(LavaDynamics.LavaConfig.getInt("chance") > rand.nextInt(1000) + 1) {
								if (debug) LavaDynamics.Logger.debug("Spawning volcanp at Chunk: " + chunk.getPos().x + "|" + chunk.getPos().z);
								spawnVolcano(chunk.getWorld(), chunk);

							} else {
								if (debug) LavaDynamics.Logger.debug("Chance Test failed");
								return;
							}
						}
					}));
				}
			}
		}
	}


	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load e) {
		worldLoaded = true;
	}
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Unload e) {
		worldLoaded = false;
	}

	public static void spawnVolcano(World worldIn, Chunk chunk) {
		//----------Setup----------// 
		//if(active) { return; }
		if(worldIn.isRemote) { return; }
		ServerWorld world = (ServerWorld) worldIn;
		//Add to cache
		VolcanoCache.addCachedVolcano(chunk);

		boolean debug = LavaDynamics.LavaConfig.getBool("debug");
		Random rand = world.getRandom();

		if(debug && world.getChunkProvider().isChunkLoaded(chunk.getPos())) {
			LavaDynamics.Logger.info("Chunk isnt loaded? Probably going to fail.");
		}

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
		//Utils.lake(world, Blocks.LAVA, center, rand);

		//lakes.
		//world.getChunkProvider().ge
		if(debug) {
			//LavaDynamics.Logger.info("Lava lake generated");
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

			ChunkPos cPos = new ChunkPos(new BlockPos(x,j,z));
			if(!world.getChunkProvider().isChunkLoaded(cPos)) {
				if(debug) LavaDynamics.Logger.info("Chunk is unloaded, trying to load");
				world.forceChunk(cPos.x, cPos.z, true);
			}

			if(debug) {
				LavaDynamics.Logger.info("Placing Lava at y=" + j);

				boolean isLoaded = world.getChunkProvider().isChunkLoaded(new ChunkPos(new BlockPos(x,j,z)));
				System.out.println("isLoaded: " + isLoaded);
			}

			world.setBlockState(new BlockPos(x,j,z), Blocks.LAVA.getDefaultState(), 3);
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
		//----------Done?----------//
	}

	public static IVolcanoGenerator getGenerator(World world, BlockPos pos) {
		Biome biome = world.getBiome(pos);

		//if(biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN || biome == Biomes.RIVER || biome == Biomes.FROZEN_RIVER) {
		//return new WaterVolcanoGen();
		//}

		return new ConeVolcanoGen();
	}
}
