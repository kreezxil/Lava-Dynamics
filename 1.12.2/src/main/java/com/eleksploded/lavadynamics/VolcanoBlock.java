package com.eleksploded.lavadynamics;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
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
		for(EntityPlayer player : world.playerEntities){
			System.out.println(pos.getDistance((int)player.posX, (int)player.posY, (int)player.posZ));
			if(pos.getDistance((int)player.posX, (int)player.posY, (int)player.posZ) >= LavaConfig.volcano.minimumDistance) {
				Volcano.genVolcano(world.getChunkFromBlockCoords(pos), world);
			}
		}
	}
	
	//Allow walking through
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }
	
	//Debug force Volcano
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(LavaConfig.general.genVolcanoDebug) {
			randomTick(worldIn,pos,state,new Random(worldIn.getSeed()));
			return true;
		} else {
			return false;
		}
    }
	
	public void forceSpawn(World world, BlockPos pos) {
		Volcano.genVolcano(world.getChunkFromBlockCoords(pos), world);
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }
}
