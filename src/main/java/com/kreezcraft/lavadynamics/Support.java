package com.kreezcraft.lavadynamics;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Support {
	public static boolean cardinalIsAir(World worldIn, BlockPos thisPos) {
		if (worldIn.getBlockState(thisPos.east()).getBlock() == Blocks.AIR
				|| worldIn.getBlockState(thisPos.east()).getBlock() == Blocks.AIR
				|| worldIn.getBlockState(thisPos.east()).getBlock() == Blocks.AIR
				|| worldIn.getBlockState(thisPos.east()).getBlock() == Blocks.AIR)
			return true;
		return false;
	}

	public static String getModID(Block blockTarget) {
		return blockTarget.getRegistryName().getResourceDomain();
	}

	public static boolean chunkNeighborsLoaded(World worldIn, BlockPos thisPos) {
		int chunkX = worldIn.getChunkFromBlockCoords(thisPos).x;
		int chunkZ = worldIn.getChunkFromBlockCoords(thisPos).z;

		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++)
				if (!worldIn.isChunkGeneratedAt(chunkX + x, chunkZ + z))
					return false;
		return true;
	}

	public static boolean compatible(Block target) {
		String[] shitMods = LavaConfig.general.ignoreTheseMods.split(",");
		if (shitMods == null)
			return true;
		return !Arrays.asList(shitMods).contains(getModID(target));
	}


	public static IBlockState getRndOre() {
		Random r = new Random();
		int ore = r.nextInt(1000);

		Block block;
		// we should pull from the config at this point but for now it will be hard
		// coded
		// 20% chance to generate ore
		// read the if statements for the chances

		if (ore <= 25)
			block = Blocks.COAL_ORE;
		else if (ore >= 26 && ore <= 30)
			block = Blocks.IRON_ORE;
		else if (ore >= 31 && ore <= 32)
			block = Blocks.GOLD_ORE;
		else if (ore >= 33 && ore <= 34)
			block = Blocks.QUARTZ_ORE;
		else if (ore >= 35 && ore <= 36)
			block = Blocks.LAPIS_ORE;
		else if (ore >= 37 && ore <= 38)
			block = Blocks.REDSTONE_ORE;
		else if (ore == 39)
			block = Blocks.DIAMOND_ORE;
		else if (ore == 40)
			block = Blocks.EMERALD_ORE;
		else if (ore == 41)
			block = Blocks.GOLD_ORE;
		else if (ore == 42)
			block = Blocks.REDSTONE_ORE;
		else {
			// this can pull from the config too for different types of non-ore blocks to
			// place
			block = Blocks.STONE;
		}
		return block.getDefaultState();
	}

	public static void makeEffect(World worldIn, BlockPos thisPos) {
		worldIn.playSound((EntityPlayer) null, thisPos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
				2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

		if (worldIn instanceof WorldServer) {
			((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) thisPos.getX() + 0.5D,
					(double) thisPos.getY() + 0.25D, (double) thisPos.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
		}

	}
}
