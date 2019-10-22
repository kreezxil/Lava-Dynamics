package com.eleksploded.lavadynamics.postgen;

import net.minecraft.world.chunk.Chunk;

public interface IPostGenEffect {
	String getName();
	void execute(Chunk chunk, int top);
}