package com.eleksploded.lavadynamics.generator;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IVolcanoGenerator {
	void generate(World world, Random random, BlockPos position);
}
