package com.eleksploded.lavadynamics.postgen;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class PostGenEffect implements IForgeRegistryEntry<PostGenEffect> {
	
	public PostGenEffect() {
		
	}
	
	abstract void execute(Chunk chunk, int top);
}
