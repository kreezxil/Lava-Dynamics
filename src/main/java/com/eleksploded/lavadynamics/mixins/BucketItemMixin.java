package com.eleksploded.lavadynamics.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.eleksploded.lavadynamics.WorldSmelting;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item {
	
	public BucketItemMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "onLiquidPlaced", at = @At("RETURN"))
	void liquidPlaced(World worldIn, ItemStack stack, BlockPos pos, CallbackInfo c) {
		if(stack.getItem() == Items.LAVA_BUCKET) {
			WorldSmelting.fluidSpread(pos, worldIn, Fluids.LAVA);
		}
	}
}
