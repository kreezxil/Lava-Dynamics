package com.eleksploded.lavadynamics.postgen;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class PostGenEffect extends ForgeRegistryEntry<PostGenEffect> {
	
	public PostGenEffect() {
		
	}
	
	public abstract void execute(Chunk chunk, int top);
}
