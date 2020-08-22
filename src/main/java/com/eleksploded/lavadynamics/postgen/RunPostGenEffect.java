package com.eleksploded.lavadynamics.postgen;

import java.util.List;
import java.util.stream.StreamSupport;

import com.eleksploded.lavadynamics.LavaDynamics;
import com.eleksploded.lavadynamics.cap.CheckedCap;
import com.eleksploded.lavadynamics.cap.IChecked;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class RunPostGenEffect {

	@SuppressWarnings("resource")
	//@SubscribeEvent
	public static void worldTick(ChunkEvent.Load event) {
		if(!event.getWorld().isRemote()) {
			ServerWorld world = (ServerWorld)event.getWorld();

			Iterable<ChunkHolder> chunks = world.getChunkProvider().chunkManager.getLoadedChunksIterable();
			int count = world.getChunkProvider().chunkManager.getLoadedChunkCount();
			
			StreamSupport.stream(chunks.spliterator(), false).skip(world.getRandom().nextInt(count)).findFirst().ifPresent(chunkHolder -> {
				Chunk chunk = chunkHolder.getChunkIfComplete();
				IChecked checked = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));
				
				if(checked.isVolcano()) {
					
					if(checked.getCooldown() > 0) {
						checked.setCooldown(checked.getCooldown() - 1);
					} else if(world.getRandom().nextInt(999)+1 <= LavaDynamics.LavaConfig.getInt("postgenchance")) {
						IForgeRegistry<PostGenEffect> reg = GameRegistry.findRegistry(PostGenEffect.class);
						ResourceLocation key = reg.getKeys().stream().skip(world.rand.nextInt((int)reg.getKeys().stream().count())).findFirst().get();
						@SuppressWarnings("unchecked")
						List<String> blacklistedEffects = (List<String>) LavaDynamics.LavaConfig.getValue("");
						if(!blacklistedEffects.contains(key.toString())) {
							reg.getValue(key).execute(chunk, checked.getTop());
						}
					}
				}
			});
		}
	}
}
