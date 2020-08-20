package com.eleksploded.lavadynamics.utils;

import java.util.ArrayList;
import java.util.List;

import com.eleksploded.lavadynamics.LavaDynamics;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class VolcanoCache {
	private static List<Chunk> CACHE  = new ArrayList<Chunk>();
	
	public static boolean isCachedVolcano(Chunk chunk) {
		return CACHE.contains(chunk);
	}
	
	public static void addCachedVolcano(Chunk chunk) {
		int size = LavaDynamics.LavaConfig.getInt("volcanoCacheSize");
		
		if(size == 0) {
			if(CACHE != null) {
				CACHE = null;
			}
			return;
		}
		
		if(CACHE.size() >= size) {
			CACHE.remove(CACHE.size() - 1);
		}
		
		CACHE.add(0, chunk);
	}
	
	public static boolean isCachedVolcano(ChunkPos pos) {
		for(Chunk c : CACHE) {
			if(c.getPos() == pos) {
				return true;
			}
		}
		return false;
	}
}
