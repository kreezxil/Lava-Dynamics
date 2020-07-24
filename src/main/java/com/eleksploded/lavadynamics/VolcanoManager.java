package com.eleksploded.lavadynamics;

import net.minecraftforge.event.world.ChunkEvent;

public class VolcanoManager {
	
	public static void chunkGen(ChunkEvent.Load e) {
		e.getChunk();
	}
}
