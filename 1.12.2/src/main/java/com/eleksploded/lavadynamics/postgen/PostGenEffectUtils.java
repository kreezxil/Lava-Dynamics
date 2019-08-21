package com.eleksploded.lavadynamics.postgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class PostGenEffectUtils {
	
	public static BlockPos getVolcanoTop(Chunk chunk, int top){
		int x = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int z = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();	
		return new BlockPos(x,top,z);
	}
}
