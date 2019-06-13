package com.eleksploded.lavadynamics;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class VolcanoGenerator extends WorldGenerator {

	//Pretty much a copy of Ice Spikes Generator
	public VolcanoGenerator() {
		//Be sure to update blocks around it, since we are after worldgen
		super(true);
	}

	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		while (worldIn.isAirBlock(position) && position.getY() > 2)
		{
			position = position.down();
		}

		if (worldIn.getBlockState(position).getBlock() != Blocks.LAVA)
		{
			return false;
		}
		else
		{
			//Edited Values, not sure what does what but these change the generation
			position = position.up(rand.nextInt(4));
			int i = rand.nextInt(12) + 7;
			int j = i / 4 + rand.nextInt(10);

			if (j > 1 && rand.nextInt(60) == 0)
			{
				position = position.up(10 + rand.nextInt(30));
			}

			for (int k = 0; k < i; ++k)
			{
				float f = (1.0F - (float)k / (float)i) * (float)j;
				int l = MathHelper.ceil(f);

				for (int i1 = -l; i1 <= l; ++i1)
				{
					float f1 = (float)MathHelper.abs(i1) - 0.25F;

					for (int j1 = -l; j1 <= l; ++j1)
					{
						float f2 = (float)MathHelper.abs(j1) - 0.25F;

						if ((i1 == 0 && j1 == 0 || f1 * f1 + f2 * f2 <= f * f) && (i1 != -l && i1 != l && j1 != -l && j1 != l || rand.nextFloat() <= 0.55F))
						{
							IBlockState iblockstate = worldIn.getBlockState(position.add(i1, k, j1));
							Block block = iblockstate.getBlock();

							if (iblockstate.getBlock().isAir(iblockstate, worldIn, position.add(i1, k, j1)))
							{
								//int up = rand.nextInt(10);


								this.setBlockWithOre(worldIn, position.add(i1, k, j1), Blocks.STONE.getDefaultState());
							}

							if (k != 0 && l > 1)
							{
								iblockstate = worldIn.getBlockState(position.add(i1, -k, j1));
								block = iblockstate.getBlock();

								if (iblockstate.getBlock().isAir(iblockstate, worldIn, position.add(i1, -k, j1)) || block == Blocks.LAVA || block == Blocks.FLOWING_LAVA)
								{
									this.setBlockWithOre(worldIn, position.add(i1, -k, j1), Blocks.STONE.getDefaultState());
								}
							}
						}
					}
				}
			}

			int k1 = j - 1;

			if (k1 < 0)
			{
				k1 = 0;
			}
			else if (k1 > 1)
			{
				k1 = 1;
			}

			for (int l1 = -k1; l1 <= k1; ++l1)
			{
				for (int i2 = -k1; i2 <= k1; ++i2)
				{
					BlockPos blockpos = position.add(l1, -1, i2);
					int j2 = 50;

					if (Math.abs(l1) == 1 && Math.abs(i2) == 1)
					{
						j2 = rand.nextInt(5);
					}

					while (blockpos.getY() > 50)
					{   	
						this.setBlockWithOre(worldIn, blockpos, Blocks.STONE.getDefaultState());
						blockpos = blockpos.down();
						--j2;

						if (j2 <= 0)
						{
							blockpos = blockpos.down(rand.nextInt(5) + 1);
							j2 = rand.nextInt(5);
						}
					}
				}
			}
			return true;
		}
		
	}

	private void setBlockWithOre(World worldIn, BlockPos blockpos, IBlockState defaultState) {
		Random rand = new Random();
		
		Block block;
		int ore = rand.nextInt(1000);
		
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
			block = Blocks.STONE;
		}
		
		this.setBlockAndNotifyAdequately(worldIn, blockpos, block.getDefaultState());
		
	}


}
