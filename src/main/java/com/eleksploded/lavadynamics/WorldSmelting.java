package com.eleksploded.lavadynamics;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WorldSmelting {

	@SubscribeEvent
	public static void onBlockUpdate(BlockEvent.NeighborNotifyEvent e) {
		if(!e.getWorld().isRemote() && LavaDynamics.LavaConfig.getBool("worldSmeltingEnabled")) {
			World world = (World) e.getWorld();

			if(e.getState().getBlock().isAir(e.getState(), world, e.getPos())) {
				return;
			}

			if(isByLava(world, e.getPos())) {
				doSmelt(e.getPos(), world, e.getState());
			}
		}
	}

	public static void fluidSpread(BlockPos posIn, World world, Fluid fluid) {
		if(!world.isRemote && LavaDynamics.LavaConfig.getBool("worldSmeltingEnabled")) {

			if(fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA) {

				for(Direction dir : Direction.values()) {
					BlockPos pos = posIn.offset(dir, 1);
					BlockState state = world.getBlockState(pos);

					doSmelt(pos, world, state);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void doSmelt(BlockPos pos, World world, BlockState state) {
		if(state.isAir(world, pos)) {
			return;
		}

		List<String> blacklist = (List<String>) LavaDynamics.LavaConfig.getValue("blacklistedBlocks");
		if(blacklist.contains(state.getBlock().getRegistryName().toString())) return;

		ItemStack stack = new ItemStack(state.getBlock().asItem(), 1);
		Inventory fakeInv = new Inventory(1);
		fakeInv.addItem(stack);

		IRecipe<?> r = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, fakeInv, world).orElse(null);

		if(r != null && r.getType() == IRecipeType.SMELTING) {
			Block output = Block.getBlockFromItem(r.getRecipeOutput().getItem());

			if(output != Blocks.AIR) {
				world.setBlockState(pos, output.getDefaultState());
				world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
			}
		}
	}

	public static boolean isByLava(World world, BlockPos pos) {
		for(Direction dir : Direction.class.getEnumConstants()) {
			Block b = world.getBlockState(pos.offset(dir, 1)).getBlock();
			if(b == Blocks.LAVA) {
				return true;
			}
		}
		return false;
	}

	public static IRecipe<?> getRecipeFromBlock(RecipeManager manager, Block block) {
		for(IRecipe<?> recipe : manager.getRecipes()) {
			Ingredient i = Ingredient.fromStacks(new ItemStack(block.asItem()));
			if(recipe.getIngredients().contains(i)) {
				return recipe;
			}
		}
		return null;
	}
}
