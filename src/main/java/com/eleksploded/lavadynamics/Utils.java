package com.eleksploded.lavadynamics;

import java.util.Random;

import com.eleksploded.lavadynamics.cap.CheckedCap;
import com.eleksploded.lavadynamics.cap.IChecked;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class Utils {
	public static boolean isVolcanoInRange(Chunk c) {
		World w = c.getWorld();
		int r = LavaDynamics.LavaConfig.getInt("volcanoDistance");

		for(int x = -r; x < r; x++) {
			for(int z = -r; x < r; z++) {
				if(!w.getChunkProvider().chunkExists(x, z)) continue;
				Chunk t = w.getChunk(x, z);

				IChecked tc = t.getCapability(CheckedCap.checkedCap).orElseThrow(() -> new RuntimeException(CheckedCap.ThrowError));
				if(tc.isVolcano()) {
					return true;
				}
			}
		}
		return false;
	}

	public static void lake(World world, Block block, BlockPos pos, Random rand) {

		boolean[] aboolean = new boolean[2048];
		int i = rand.nextInt(4) + 4;

		for(int j = 0; j < i; ++j) {
			double d0 = rand.nextDouble() * 6.0D + 3.0D;
			double d1 = rand.nextDouble() * 4.0D + 2.0D;
			double d2 = rand.nextDouble() * 6.0D + 3.0D;
			double d3 = rand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
			double d4 = rand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
			double d5 = rand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

			for(int l = 1; l < 15; ++l) {
				for(int i1 = 1; i1 < 15; ++i1) {
					for(int j1 = 1; j1 < 7; ++j1) {
						double d6 = ((double)l - d3) / (d0 / 2.0D);
						double d7 = ((double)j1 - d4) / (d1 / 2.0D);
						double d8 = ((double)i1 - d5) / (d2 / 2.0D);
						double d9 = d6 * d6 + d7 * d7 + d8 * d8;
						if (d9 < 1.0D) {
							aboolean[(l * 16 + i1) * 8 + j1] = true;
						}
					}
				}
			}

			for(int l1 = 0; l1 < 16; ++l1) {
				for(int i3 = 0; i3 < 16; ++i3) {
					for(int i4 = 0; i4 < 8; ++i4) {
						if (aboolean[(l1 * 16 + i3) * 8 + i4]) {
							world.setBlockState(pos.add(l1, i4, i3), i4 >= 4 ? Blocks.AIR.getDefaultState() : block.getDefaultState(), 2);
						}
					}
				}
			}

			if (block.getDefaultState().getMaterial() == Material.LAVA) {
				for(int j2 = 0; j2 < 16; ++j2) {
					for(int k3 = 0; k3 < 16; ++k3) {
						for(int k4 = 0; k4 < 8; ++k4) {
							boolean flag1 = !aboolean[(j2 * 16 + k3) * 8 + k4] && (j2 < 15 && aboolean[((j2 + 1) * 16 + k3) * 8 + k4] || j2 > 0 && aboolean[((j2 - 1) * 16 + k3) * 8 + k4] || k3 < 15 && aboolean[(j2 * 16 + k3 + 1) * 8 + k4] || k3 > 0 && aboolean[(j2 * 16 + (k3 - 1)) * 8 + k4] || k4 < 7 && aboolean[(j2 * 16 + k3) * 8 + k4 + 1] || k4 > 0 && aboolean[(j2 * 16 + k3) * 8 + (k4 - 1)]);
							if (flag1 && (k4 < 4 || rand.nextInt(2) != 0) && world.getBlockState(pos.add(j2, k4, k3)).getMaterial().isSolid()) {
								world.setBlockState(pos.add(j2, k4, k3), Blocks.STONE.getDefaultState(), 2);
							}
						}
					}
				}
			}
		}
	}
}
