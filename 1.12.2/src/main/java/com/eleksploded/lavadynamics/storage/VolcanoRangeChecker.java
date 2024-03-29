package com.eleksploded.lavadynamics.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.eleksploded.lavadynamics.threaded.GeneratorServerHandler;
import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.ListenableFutureTask;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VolcanoRangeChecker {

	private static final Map<Integer, VolcanoRangeChecker> map = new HashMap<>();
	public static VolcanoRangeChecker getForDim(int id) {
		if(map.containsKey(id)) {
			return map.get(id);
		} else {
			map.put(id, new VolcanoRangeChecker(id));
			return map.get(id);
		}
	}

	final int dim;
	Set<Chunk> loadedVolcanoes = new HashSet<Chunk>();
	Set<ChunkPos> knownVolcanoes = new HashSet<ChunkPos>();

	public VolcanoRangeChecker(int dimId) {
		dim = dimId;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	private void onChunkLoad(ChunkEvent.Load e) {
		if(e.getWorld().provider.getDimension() == dim && e.getChunk().getCapability(CheckedCap.checkedCap, null).isVolcano()) {
			loadedVolcanoes.add(e.getChunk());
			if(!knownVolcanoes.contains(e.getChunk().getPos())) {
				knownVolcanoes.add(e.getChunk().getPos());
			}
		}
	}

	@SubscribeEvent
	private void onChunkUnLoad(ChunkEvent.Unload e) {
		if(e.getWorld().provider.getDimension() == dim && loadedVolcanoes.contains(e.getChunk())) {
			loadedVolcanoes.remove(e.getChunk());
		}
	}

	public boolean isVolcanoInRange(Chunk chunk, int blockRange) {
		World world = chunk.getWorld();
		List<ChunkPos> list = new ArrayList<>();
		int chunkRange = blockRange << 4;

		for(int x = chunk.x - chunkRange; x < chunk.x + chunkRange; x++) {
			for(int z = chunk.z - chunkRange; z < chunk.z + chunkRange; z++) {
				list.add(new ChunkPos(x,z));
			}
		}
		list.remove(chunk.getPos());
		boolean isInKnown = list.stream().anyMatch(knownVolcanoes::contains); //Check for any known volcanos
		if(isInKnown) {
			return true;
		} else {
			for(ChunkPos pos : list) {
				if(!world.isChunkGeneratedAt(pos.x, pos.z)) continue; //Chunk has never been generated, so can't be a volcano

				//Now we have to load the chunk and check :(
				if(world.getChunk(pos.x, pos.z).getCapability(CheckedCap.checkedCap, null).isVolcano()) return true; //TODO: NOT THREADSAFE
			}
		}

		return false;
	}

	public Future<Boolean> isVolcanoInRangeThreaded(MinecraftServer server, Chunk chunk, int blockRange) {
		synchronized(knownVolcanoes) {
			final World world = chunk.getWorld();
			List<ChunkPos> list = new ArrayList<>();
			final int chunkRange = blockRange << 4;

			for(int x = chunk.x - chunkRange; x < chunk.x + chunkRange; x++) {
				for(int z = chunk.z - chunkRange; z < chunk.z + chunkRange; z++) {
					list.add(new ChunkPos(x,z));
				}
			}
			list.remove(chunk.getPos());
			boolean isInKnown = list.stream().anyMatch(knownVolcanoes::contains); //Check for any known volcanos
			if(isInKnown) {
				return ListenableFutureTask.create(Callables.returning(true));
			} else {
				Callable<Boolean> callable = new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						for(ChunkPos pos : list) {
							if(!world.isChunkGeneratedAt(pos.x, pos.z)) continue; //Chunk has never been generated, so can't be a volcano
							
							Chunk chunk = GeneratorServerHandler.requestChunk(pos).getOncePresent(); // Get chunk threadsafely
							if(chunk.getCapability(CheckedCap.checkedCap, null).isVolcano()) return true; //Test Cap
						}
						return false;
					}
				};
				return ListenableFutureTask.create(callable);
			}
		}
	}

	public List<Chunk> getLoadedVolcanoes() {
		return new ArrayList<Chunk>(loadedVolcanoes);
	}

	public List<ChunkPos> getKnownVolcanoes() {
		return new ArrayList<ChunkPos>(knownVolcanoes);
	}
}
