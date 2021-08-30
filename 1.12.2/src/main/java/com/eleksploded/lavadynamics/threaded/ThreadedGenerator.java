package com.eleksploded.lavadynamics.threaded;

import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang3.tuple.Pair;

import com.eleksploded.lavadynamics.LavaConfig;
import com.eleksploded.lavadynamics.Volcano;
import com.eleksploded.lavadynamics.storage.CheckedCap;
import com.eleksploded.lavadynamics.storage.VolcanoRangeChecker;
import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.ListenableFutureTask;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.chunk.Chunk;

public class ThreadedGenerator extends Thread {
	private static ThreadedGenerator instance = null;

	MinecraftServer server;
	private Deque<Chunk> chunkQueue = new ConcurrentLinkedDeque<>();
	private Deque<Pair<Chunk, Future<Boolean>>> completeQueue = new ConcurrentLinkedDeque<>();
	private Completer completer;
	private Random rand;

	private ThreadedGenerator(MinecraftServer server) {
		instance = this;
		this.server = server;
		this.setDaemon(true);
		rand = new Random();

		completer = new Completer();
		completer.start();
	}

	public static ThreadedGenerator getOrCreate(MinecraftServer server) {
		if(instance == null) {
			if(server == null) {
				throw new RuntimeException("Attempted to create threaded generator without server reference");
			}
			new ThreadedGenerator(server);
		}
		return instance;
	}


	public void queue(Chunk c) {
		chunkQueue.add(c);
	}

	public void end() {
		instance = null;
	}

	public void run() {
		while(server.isServerRunning() && instance != null) {
			if(!chunkQueue.isEmpty()) {
				Chunk c = chunkQueue.poll();
				if(LavaConfig.volcano.disaster) {
					completeQueue.add(Pair.of(c, ListenableFutureTask.create(Callables.returning(false))));
				} else if(isChunkGeneratable(c)) {
					Future<Boolean> rangeCheck = VolcanoRangeChecker.getForDim(c.getWorld().provider.getDimension()).isVolcanoInRangeThreaded(server, c, LavaConfig.volcano.distance);
					completeQueue.add(Pair.of(c, rangeCheck));
				}
			}
		}
		instance = null;
	}

	private boolean isChunkGeneratable(Chunk c) {

		if(c.getCapability(CheckedCap.checkedCap, null).isChecked()) {
			return false;
		}

		c.getCapability(CheckedCap.checkedCap, null).check();

		if(LavaConfig.volcano.protectChunks && !c.getTileEntityMap().isEmpty()) {
			return false;
		}

		if(!LavaConfig.volcano.spawnChunks && c.getWorld().isSpawnChunk(c.x, c.z)) {
			return false;
		}

		return true;
	}
	
	public static boolean isActive() {
		return instance != null;
	}

	private class Completer extends Thread {

		private Completer() {
			this.setDaemon(true);
		}

		public void run() {
			while(instance != null) {
				if(!completeQueue.isEmpty()) {
					if(completeQueue.peek().getRight().isDone()) {
						try {
							Pair<Chunk, Future<Boolean>> pair = completeQueue.poll();

							if(!pair.getRight().get()) {
								int num = rand.nextInt(100) + 1;
								if(num <= LavaConfig.volcano.volcanoChance) {
									Chunk chunk = pair.getLeft();
									server.addScheduledTask(() -> {
										Volcano.genVolcano(chunk, chunk.getWorld()); //TODO: Threaded Volcano Building
									});
								}
							}
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
