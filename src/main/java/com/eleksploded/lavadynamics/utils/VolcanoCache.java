package com.eleksploded.lavadynamics.utils;

import org.apache.commons.lang3.tuple.Pair;

import com.eleksploded.lavadynamics.LavaDynamics;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class VolcanoCache {
	private static IndexedMap<ChunkPos, Boolean> CACHE  = new IndexedMap<ChunkPos, Boolean>();
	
	public static boolean isCached(Chunk chunk) {
		return CACHE.containsKey(chunk.getPos());
	}
	
	public static boolean isCached(ChunkPos pos) {
		return CACHE.containsKey(pos);
	}
	
	public static CacheResult getCacheResult(Chunk chunkIn) {
		for(Pair<ChunkPos, Boolean> e : CACHE.pairSet()) {
			if(e.getLeft() == chunkIn.getPos()) {
				return e.getRight() ? CacheResult.VOLCANO : CacheResult.NO_VOLCANO;
			}
		}
		return CacheResult.NON_CACHED;
	}
	
	public static void addCachedChunk(ChunkPos pos, boolean volcano) {
		int size = LavaDynamics.LavaConfig.getInt("cacheSize");
		
		if(size == 0) {
			if(CACHE != null) {
				CACHE = null;
			}
			return;
		}
		
		if(CACHE.containsKey(pos)) {
			return;
		}
		
		if(CACHE.size() >= size) {
			CACHE.remove(CACHE.size() - 1);
		}
		
		CACHE.add(0, pos, volcano);
	}
	
	public static void addCachedChunk(Chunk chunk, boolean volcano) {
		addCachedChunk(chunk.getPos(), volcano);
	}
	
	public static CacheResult getCacheResult(ChunkPos pos) {
		for(Pair<ChunkPos, Boolean> e : CACHE.pairSet()) {
			if(e.getLeft() == pos) {
				return e.getRight() ? CacheResult.VOLCANO : CacheResult.NO_VOLCANO;
			}
		}
		return CacheResult.NON_CACHED;
	}
	
	public enum CacheResult {
		VOLCANO, NO_VOLCANO, NON_CACHED
	}
}
