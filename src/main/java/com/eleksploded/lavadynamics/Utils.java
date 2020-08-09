package com.eleksploded.lavadynamics;

import com.eleksploded.lavadynamics.cap.CheckedCap;
import com.eleksploded.lavadynamics.cap.IChecked;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public class Utils {
	public static boolean isVolcanoInRange(ServerWorld world, Chunk c) {
		boolean debug = LavaDynamics.LavaConfig.getBool("debug");
		
		int r = LavaDynamics.LavaConfig.getInt("volcanoDistance");
		if (debug) LavaDynamics.Logger.debug("Range is " + r);
		//TODO: Something is borked here... The loop isnt running I think. Causes hang on load
		for(int x = -r; x > r; x++) {
			for(int z = -r; x > r; z++) {
				Chunk t = world.getChunk(x, z);
				
				if (debug) LavaDynamics.Logger.debug("Range check on " + t.getPos().x + "|" + t.getPos().z);
				if(!world.getChunkProvider().isChunkLoaded(t.getPos())) {
					if (debug) LavaDynamics.Logger.debug("Chunk isnt loaded. Skipping");
					continue;
				}
				
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
}
