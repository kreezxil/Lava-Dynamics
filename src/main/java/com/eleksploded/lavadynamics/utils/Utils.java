package com.eleksploded.lavadynamics.utils;

import java.util.ArrayList;
import java.util.List;

import com.eleksploded.lavadynamics.LavaDynamics;
import com.eleksploded.lavadynamics.cap.CheckedCap;
import com.eleksploded.lavadynamics.cap.IChecked;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerWorld;

public class Utils {
	@SuppressWarnings("deprecation")
	public static boolean isVolcanoInRange(ServerWorld world, Chunk c) {
		boolean debug = LavaDynamics.LavaConfig.getBool("debug");
		
		int r = LavaDynamics.LavaConfig.getInt("volcanoDistance");
		if (debug) LavaDynamics.Logger.debug("Range is " + r);

		for(int x = -r; x < r; x++) {
			for(int z = -r; z < r; z++) {
				
				ChunkPos pos = new ChunkPos(c.getPos().x + x, c.getPos().z + z);
				
				if(VolcanoCache.isCachedVolcano(pos)) {
					if (debug) LavaDynamics.Logger.debug("Found Volcano from cache");
					return true;
				}
				
				BlockPos from = new BlockPos(pos.getXStart(), 0, pos.getZStart());
				BlockPos to = new BlockPos(pos.getXEnd(), 255, pos.getZEnd());
				
				if (debug) LavaDynamics.Logger.debug("Range check on " + (c.getPos().x + x) + "|" + (c.getPos().z + z));
				if(!world.isAreaLoaded(from, to)) {
					if (debug) LavaDynamics.Logger.debug("Chunk isnt loaded. Skipping");
					continue;
				}
				
				Chunk t = (Chunk) world.getChunk(c.getPos().x + x, c.getPos().z + z);
				IChecked tc = t.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));
				if(tc.isVolcano()) {
					if (debug) LavaDynamics.Logger.debug("Found Volcano");
					return true;
				} else {
					if (debug) LavaDynamics.Logger.debug("No Volcano");
				}
			}
		}
		return false;
	}
	
	public static List<Chunk> getLoadedChunks(ServerWorld world) {
		List<Chunk> list = new ArrayList<Chunk>();
		@SuppressWarnings("resource")
		Iterable<ChunkHolder> chunks = world.getChunkProvider().chunkManager.getLoadedChunksIterable();
		chunks.forEach(c -> {
			Chunk ch = c.getChunkIfComplete();
			if(ch != null) list.add(ch);
		});
		return list;
	}
}
