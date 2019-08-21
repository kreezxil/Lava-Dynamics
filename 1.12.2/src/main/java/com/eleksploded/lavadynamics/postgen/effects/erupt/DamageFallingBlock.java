package com.eleksploded.lavadynamics.postgen.effects.erupt;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class DamageFallingBlock extends EntityFallingBlock {
	
	public DamageFallingBlock(World world){
		super(world);
	}
	
	public DamageFallingBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
		super(worldIn, x, y, z, fallingBlockState);
	}
	
	public void onCollideWithPlayer(EntityPlayer entityIn)
    {
		System.out.println("Hello");
    }

}
