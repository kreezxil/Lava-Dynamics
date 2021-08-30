package com.eleksploded.lavadynamics.threaded;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

import com.eleksploded.lavadynamics.LavaConfig;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GeneratorServerHandler {
	
	private static Deque<WaitingValue<Chunk>> queue = new LinkedBlockingDeque<>();
	
	public static WaitingValue<Chunk> requestChunk(ChunkPos pos) {
		WaitingValue<Chunk> wv = new WaitingValue<>(pos);
		queue.add(wv);
		return wv;
	}
	
	@SubscribeEvent
	public static void getChunks(TickEvent.WorldTickEvent e) {
		if(!queue.isEmpty()) {
			int iterations = Math.min(queue.size(), LavaConfig.threadedOptions.chunksPerTick);
			for(int i = 0; i < iterations; i++) {
				WaitingValue<Chunk> wv = queue.poll();
				ChunkPos pos = (ChunkPos) wv.getArgs()[0];
				wv.serve(e.world.getChunk(pos.x, pos.z));
			}
		}
	}
	
	@SubscribeEvent
	public static void loadWorld(WorldEvent.Load e) {
		if(!ThreadedGenerator.isActive()) {
			ThreadedGenerator.getOrCreate(e.getWorld().getMinecraftServer());
		}
	}
	
	@SubscribeEvent
	public static void loadChunk(ChunkEvent.Load e) {
		ThreadedGenerator.getOrCreate(e.getWorld().getMinecraftServer()).queue(e.getChunk());
	}
	
	@EventHandler
	public static void serverClosing(FMLServerStoppingEvent e) {
		ThreadedGenerator.getOrCreate(null).end();
	}
}
