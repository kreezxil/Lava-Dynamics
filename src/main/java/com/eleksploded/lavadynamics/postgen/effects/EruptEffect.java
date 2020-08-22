package com.eleksploded.lavadynamics.postgen.effects;

import java.util.Random;

import com.eleksploded.lavadynamics.LavaDynamics;
import com.eleksploded.lavadynamics.postgen.PostGenEffect;

import net.minecraft.block.Blocks;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EruptEffect extends PostGenEffect {
	
	public EruptEffect() {
		this.setRegistryName(new ResourceLocation(LavaDynamics.ModId, "erupt"));
	}
	
	public void execute(Chunk chunk, int topIn){
		World world = chunk.getWorld();
		if(world.isRemote) return;
		  
		Random rand = new Random();
		int x = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int z = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();	
		BlockPos top = new BlockPos(x,topIn,z);
		
		BlockPos.getAllInBox(top.add(-3, -3, -3), top.add(3, 3, 3)).forEach(pos -> {
			FallingBlockEntity entity = new FallingBlockEntity(world, pos.getX(), pos.getY()+2, pos.getZ(), Blocks.STONE.getDefaultState());
			if(rand.nextInt(100)+1 < 20){
				world.addEntity(entity);
				entity.fallTime = 1;
				entity.addVelocity(range(rand,-1,1), range(rand,.1F,1), range(rand,-1,1));
			}	
		});
		
		world.createExplosion(null, x, topIn+2, z, 5, true, Explosion.Mode.DESTROY);
	}
	
	private float range(Random rand, float min, float max){
		return min + rand.nextFloat() * (max - min);
	}
}
