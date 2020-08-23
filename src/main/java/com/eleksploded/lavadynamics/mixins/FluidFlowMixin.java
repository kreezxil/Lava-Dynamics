package com.eleksploded.lavadynamics.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.eleksploded.lavadynamics.WorldSmelting;

import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

@Mixin(FlowingFluid.class)
public abstract class FluidFlowMixin extends Fluid {

	@Shadow
	protected abstract void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state);
	
	@SuppressWarnings("deprecation")
	@Overwrite
	protected void flowInto(IWorld worldIn, BlockPos pos, BlockState blockStateIn, Direction direction, FluidState fluidStateIn) {
		if (blockStateIn.getBlock() instanceof ILiquidContainer) {
	         ((ILiquidContainer)blockStateIn.getBlock()).receiveFluid(worldIn, pos, blockStateIn, fluidStateIn);
	      } else {
	         if (!blockStateIn.isAir()) {
	            this.beforeReplacingBlock(worldIn, pos, blockStateIn);
	         }
	         
	         worldIn.setBlockState(pos, fluidStateIn.getBlockState(), 3);
	         
	         WorldSmelting.fluidSpread(pos, (World)worldIn, fluidStateIn.getFluid());
	      }
	}
}
