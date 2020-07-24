package com.eleksploded.lavadynamics;

import com.eleksploded.lavadynamics.cap.CheckedCap;
import com.eleksploded.lavadynamics.cap.IChecked;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= "lavadynamics")
public class VolcanoManager {
	
	@SubscribeEvent
	public static void chunkGen(ChunkEvent.Load e) {
		Chunk chunk = ((Chunk)e.getChunk());
		IChecked ch = chunk.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));
	
		System.out.println(String.format("Chunk [%d, %d] is " + ch.isChecked(), chunk.getPos().x, chunk.getPos().z));
	}
}
