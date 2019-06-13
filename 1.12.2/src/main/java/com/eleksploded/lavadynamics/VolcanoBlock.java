package com.eleksploded.lavadynamics;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class VolcanoBlock extends Block {

	//Normal Block Setup
	public VolcanoBlock() {
		super(Material.ROCK);
		this.setRegistryName(new ResourceLocation(Reference.MODID, "VolcanoBlock"));
		this.setUnlocalizedName(Reference.MODID + ":VolcanoBlock");
		//Needed to receive random ticks
		this.setTickRandomly(true);
	}
	
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
		//Random tick happened so spawn volcano
		Volcano.genVolcano(world.getChunkFromBlockCoords(pos), world);
	}
	
	//Allow walking through
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }
	
	//Debug force Volcano
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(Config.genVolcanoDebug) {
			randomTick(worldIn,pos,state,new Random(worldIn.getSeed()));
			return true;
		} else {
			return false;
		}
    }
	 
}
