package com.eleksploded.lavadynamics;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class WorldSmelting {
	
	@SubscribeEvent
	public static void Smelt(BlockEvent event) {
		//Get World instance & Block
		World world = event.getWorld();
		IBlockState state = world.getBlockState(event.getPos());
		
		//Check for lava
		if(state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA){
			//Check 'n Smelt in all directions
			CheckNSmelt(world, event.getPos().up());
			CheckNSmelt(world, event.getPos().down());
			CheckNSmelt(world, event.getPos().north());
			CheckNSmelt(world, event.getPos().south());
			CheckNSmelt(world, event.getPos().east());
			CheckNSmelt(world, event.getPos().west());
		//Maybe Lava is already placed, check for that
		} else if(isByLava(world, event.getPos())) {
			//CheckNSmelt if by lava
			CheckNSmelt(world, event.getPos());
		}
	}

	private static boolean isByLava(World world, BlockPos pos) {
		//Relatively Self explanatory, check all sides for lava
		if(world.getBlockState(pos.up()).getBlock() == Blocks.LAVA || world.getBlockState(pos.up()).getBlock() ==  Blocks.FLOWING_LAVA) {
			return true;
		} else if(world.getBlockState(pos.down()).getBlock() == Blocks.LAVA || world.getBlockState(pos.down()).getBlock() ==  Blocks.FLOWING_LAVA){
			return true;
		} else if(world.getBlockState(pos.north()).getBlock() == Blocks.LAVA || world.getBlockState(pos.north()).getBlock() ==  Blocks.FLOWING_LAVA) {
			return true;
		} else if(world.getBlockState(pos.south()).getBlock() == Blocks.LAVA || world.getBlockState(pos.south()).getBlock() ==  Blocks.FLOWING_LAVA) {
			return true;
		} else if(world.getBlockState(pos.east()).getBlock() == Blocks.LAVA || world.getBlockState(pos.east()).getBlock() ==  Blocks.FLOWING_LAVA) {
			return true;
		} else if(world.getBlockState(pos.west()).getBlock() == Blocks.LAVA || world.getBlockState(pos.west()).getBlock() ==  Blocks.FLOWING_LAVA) {
			return true;
		} else {
			return false;
		}
	}

	private static void CheckNSmelt(World world, BlockPos pos) {
		//Get Variables cause I'm a lazy programmer and don't want to type the things multiple times
		Block block = world.getBlockState(pos).getBlock();
		FurnaceRecipes furn = FurnaceRecipes.instance();
		
		//Check if Block has a smelting result
		if(furn.getSmeltingResult(new ItemStack(block)) != ItemStack.EMPTY) {
			//Check if the result is a block
			if(furn.getSmeltingResult(new ItemStack(block)).getItem() instanceof ItemBlock) {
				//Set Resulting Block in world
				Block set = Block.getBlockFromItem(furn.getSmeltingResult(new ItemStack(block)).getItem());
				world.setBlockState(pos, set.getDefaultState());
			}
		}
	}
}
