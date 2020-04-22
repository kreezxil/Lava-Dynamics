package com.eleksploded.lavadynamics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class WorldSmelting {
	
	private static List<String> blacklist = new ArrayList<String>();
	
	@SubscribeEvent
	public static void worldLoad(WorldEvent.Load event){
		Collections.addAll(blacklist, LavaConfig.worldSmelt.blacklist);
	}
	
	@SubscribeEvent
	public static void Smelt(BlockEvent event) {
		//System.out.println("Place");
		if(LavaDynamics.mineralogy) {
			//return;
		}
		
		//Get World instance & Block
		World world = event.getWorld();
		IBlockState state = world.getBlockState(event.getPos());
		
		if(state.getBlock() == Blocks.AIR) {
			return;
		}
		
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

	//Credit to SirLyle (https://github.com/SirLyle) for this code
	private static void CheckNSmelt(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		ItemStack input = Item.getItemFromBlock(block).getHasSubtypes() ?
                new ItemStack(block, 1, block.getMetaFromState(state)) : new ItemStack(block);
        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
        //Check if Block has a smelting result that is a block
        if(!result.isEmpty() && result.getItem() instanceof ItemBlock && block instanceof BlockAir) {
            @SuppressWarnings("deprecation")
			final IBlockState newState = ((ItemBlock) result.getItem()).getBlock().getStateFromMeta(result.getMetadata());
            //Set Resulting Block in world
            if(!blacklist.contains(newState.getBlock().getRegistryName().toString()))
                world.setBlockState(pos,newState);
        }
	}
}
