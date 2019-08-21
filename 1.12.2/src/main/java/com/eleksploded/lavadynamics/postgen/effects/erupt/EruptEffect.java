package com.eleksploded.lavadynamics.postgen.effects.erupt;

import java.util.Random;

import com.eleksploded.lavadynamics.postgen.IPostGenEffect;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EruptEffect implements IPostGenEffect {
	
	public String getName() {
		return "erupt";
	}
	
	public void execute(Chunk chunk, int topIn){
		World world = chunk.getWorld();
		if(world.isRemote) return;
		  
		Random rand = new Random();
		int x = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int z = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();	
		BlockPos top = new BlockPos(x,topIn,z);
		
		for(MutableBlockPos pos : BlockPos.getAllInBoxMutable(top.add(-3, -3, -3), top.add(3, 3, 3))){
			EntityFallingBlock entity = new EntityFallingBlock(world, pos.getX(), pos.getY()+2, pos.getZ(), Blocks.STONE.getDefaultState());
			if(rand.nextInt(100)+1 < 20){
				world.spawnEntity(entity);
				entity.fallTime = 1;
				entity.addVelocity(range(rand,-1,1), range(rand,.1F,1), range(rand,-1,1));
			}	
		} 
		world.newExplosion(null, x, topIn+2, z, 5, true,true);
	}
	
	private float range(Random rand, float min, float max){
		return min + rand.nextFloat() * (max - min);
	}
}
