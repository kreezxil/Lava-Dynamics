package com.eleksploded.lavadynamics.postgen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import com.eleksploded.lavadynamics.LavaDynamics;
import com.eleksploded.lavadynamics.cap.CheckedCap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class PostGenEffectThread implements Runnable {

	ServerWorld world;

	int oldTick = 0;
	int newTick = 1;
	boolean running = true;

	boolean success = false;;

	public PostGenEffectThread(ServerWorld world) {
		this.world = world;
	}
	
	public void unload() {
		running = false;
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {
		boolean debug = LavaDynamics.LavaConfig.getBool("postgendebug");
		
		oldTick = new Long(world.getGameTime()).intValue();
		newTick = oldTick + 1;//1LavaDynamics.LavaConfig.getInt("PostGenEffectCooldown");
		
		while(running) {

			int tickTime = new Long(world.getGameTime()).intValue();

			if(tickTime < oldTick) {
				oldTick = tickTime;
				newTick = oldTick + 1;//LavaDynamics.LavaConfig.getInt("PostGenEffectCooldown");
			}

			if(tickTime >= oldTick + 1) {
				oldTick++;

				if(newTick < oldTick) {
					success = false;

					Iterable<ChunkHolder> chunks = world.getChunkProvider().chunkManager.getLoadedChunksIterable();

					int count = world.getChunkProvider().chunkManager.getLoadedChunkCount();
					if(count <= 0) {
						return;
					}
					StreamSupport.stream(chunks.spliterator(), false).skip(world.getRandom().nextInt(count)).findFirst().ifPresent(chunkHolder -> {
						Chunk chunk = chunkHolder.getChunkIfComplete();
						if(chunk == null) return;
						if(debug) LavaDynamics.Logger.info("Random Chunk " + chunk.getPos().toString() + " for postgen Effect");
						chunk.getCapability(CheckedCap.checkedCap).ifPresent(checked -> {
							if(checked.isVolcano()) {
								if(checked.getCooldown() > 0) {
									checked.setCooldown(checked.getCooldown() - 1);
								} else {
									IForgeRegistry<PostGenEffect> reg = GameRegistry.findRegistry(PostGenEffect.class);
									ResourceLocation key = reg.getKeys().stream().skip(world.rand.nextInt((int)reg.getKeys().stream().count())).findFirst().get();
									@SuppressWarnings("unchecked")
									List<String> blacklistedEffects = (List<String>) LavaDynamics.LavaConfig.getValue("blacklistEffects");
									if(!blacklistedEffects.contains(key.toString())) {
										if(debug) LavaDynamics.Logger.info("Running effect" + key.toString() + " on chunk " + chunk.getPos().toString());
										reg.getValue(key).execute(chunk, checked.getTop());
										success = true;
									}
								}
							}
						});
					});
					if(success) {
						oldTick = tickTime;
						newTick = oldTick +1;// LavaDynamics.LavaConfig.getInt("PostGenEffectCooldown");
					}
				}
			}
			if(debug) {
				//LavaDynamics.Logger.info(oldTick + " : " + newTick);
			}
		}
	}

	@Mod.EventBusSubscriber(modid = LavaDynamics.ModId)
	public static class PostGenEffectThreadHandler {
		
		static Map<ResourceLocation, PostGenEffectThread> pge = new HashMap<ResourceLocation, PostGenEffectThread>();
		static Map<ResourceLocation, Thread> pget = new HashMap<ResourceLocation, Thread>();

		@SubscribeEvent
		public static void worldLoad(WorldEvent.Load e) {
			if(!e.getWorld().isRemote()) {
				@SuppressWarnings("unchecked")
				List<String> validDims = (List<String>) LavaDynamics.LavaConfig.getValue("validDims");
				
				ServerWorld world = (ServerWorld) e.getWorld();
				String dimKey = world.getDimensionKey().func_240901_a_().toString();
								
				if(validDims.contains(dimKey)) {
					LavaDynamics.Logger.info("Starting PostGenEffectThread for " + dimKey);
					PostGenEffectThread i = new PostGenEffectThread(world);
					Thread t = new Thread(i);
					pge.put(world.getDimensionKey().getRegistryName(), i);
					pget.put(world.getDimensionKey().getRegistryName(), t);
					t.start();
				}
			}
		}

		@SubscribeEvent
		public static void worldUnload(WorldEvent.Unload e) {
			if(!e.getWorld().isRemote()) {
				ServerWorld world = (ServerWorld) e.getWorld();
				ResourceLocation key = world.getDimensionKey().func_240901_a_();
				
				if(pge.containsKey(key)) {
					LavaDynamics.Logger.info("Stopping PostGenEffectThread for " + key.toString());
					pge.get(key).unload();
				}
			}
		}
	}
}
