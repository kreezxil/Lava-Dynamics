package com.kreezcraft.lavadynamics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Eruption {
	public static void do_erupt(World worldIn, BlockPos thisPos) {
		//System.out.print("got authorized to make a volcano");
		Long t = worldIn.getTotalWorldTime();
		if (t % (LavaConfig.volcanoSettings.minutes * 20) != 0) {
			//System.out.println("modulo check was "+ t % (LavaConfig.volcanoSettings.minutes * 20));
			return; // only every minute we check
		}
		//System.out.println("attempting to make a volcano");
		Random chance = new Random();
		int Volcano = chance.nextInt(100);
		// debug("Volcano chance is " + Volcano);
		if (LavaDynamics.volcanoGen)
			return; // don't start another volcano until the current one is done
		if (Volcano <= LavaConfig.volcanoSettings.volcanoChance) {
			// debug("checking to see if 2 blocks up is air");
			if (thisPos.getY() <= LavaConfig.volcanoSettings.maxYlevel
					&& worldIn.getBlockState(thisPos.up(2)) != Blocks.AIR.getDefaultState()) {
				// debug("checking to see if lava is below y 69");
				List<BlockPos> theShaft = new ArrayList();
				String shaftType = LavaConfig.shaftSettings.shaftSize;
				if (shaftType.equalsIgnoreCase("random")) {
					switch (chance.nextInt(3)) {
					case 0:
						shaftType = "small";
						break;
					case 1:
						shaftType = "medium";
						break;
					default:
						shaftType = "large";
					}
				}
				if (shaftType.equalsIgnoreCase("small")) {
					theShaft.add(thisPos);
				} else if (shaftType.equalsIgnoreCase("medium")) {
					theShaft.add(thisPos);
					if (worldIn.getBlockState(thisPos.east()).getBlock() == Blocks.LAVA)
						theShaft.add(thisPos.east());

					if (worldIn.getBlockState(thisPos.west()).getBlock() == Blocks.LAVA)
						theShaft.add(thisPos.west());

					if (worldIn.getBlockState(thisPos.south()).getBlock() == Blocks.LAVA)
						theShaft.add(thisPos.south());

					if (worldIn.getBlockState(thisPos.north()).getBlock() == Blocks.LAVA)
						theShaft.add(thisPos.north());

				} else if (shaftType.equalsIgnoreCase("large")) {
					theShaft.add(thisPos);
					if (worldIn.getBlockState(thisPos.east()).getBlock() == Blocks.LAVA)
						theShaft.add(thisPos.east());

					if (worldIn.getBlockState(thisPos.west()).getBlock() == Blocks.LAVA)
						theShaft.add(thisPos.west());

					if (worldIn.getBlockState(thisPos.south()).getBlock() == Blocks.LAVA)
						theShaft.add(thisPos.south());

					if (worldIn.getBlockState(thisPos.north()).getBlock() == Blocks.LAVA)
						theShaft.add(thisPos.north());
					// --------------------------------------
					BlockPos temp = thisPos;
					temp = thisPos.east();
					temp = thisPos.north();
					if (worldIn.getBlockState(temp).getBlock() == Blocks.LAVA)
						theShaft.add(temp);

					temp = thisPos.east();
					temp = thisPos.south();
					if (worldIn.getBlockState(temp).getBlock() == Blocks.LAVA)
						theShaft.add(temp);

					temp = thisPos.west();
					temp = thisPos.north();
					if (worldIn.getBlockState(temp).getBlock() == Blocks.LAVA)
						theShaft.add(temp);

					temp = thisPos.west();
					temp = thisPos.south();
					if (worldIn.getBlockState(temp).getBlock() == Blocks.LAVA)
						theShaft.add(temp);

				} else {
					// you're a dumb ass that's not one of the config values!
					return;// consider it done
				}
				if (thisPos.getY() <= LavaConfig.volcanoSettings.psuedoSurface) {
					LavaDynamics.volcanoGen = true;
					int diff = LavaConfig.volcanoSettings.psuedoSurface - thisPos.getY();
					int extra = chance.nextInt(LavaConfig.plumes.extraHt) + LavaConfig.plumes.minHt;
					int vent = chance.nextInt(diff + extra + LavaConfig.plumes.minHt);
					int i;
					int y;
					for (i = 0; i < vent; i++) {
						for (y = 0; y < theShaft.size(); y++) {
							//worldIn.setBlockState(theShaft.get(y).up(i), Blocks.AIR.getDefaultState());
							//worldIn.setBlockState(theShaft.get(y).up(i), Blocks.LAVA.getDefaultState());
							worldIn.setBlockState(theShaft.get(y).up(i), Blocks.MAGMA.getDefaultState());
						}
						worldIn.setBlockState(theShaft.get(y-1).up(i-1), Blocks.LAVA.getDefaultState());
						i=i-3;
						worldIn.setBlockState(theShaft.get(y-1).up(i), Blocks.TNT.getDefaultState());
						// worldIn.setBlockState(thisPos.up(i), Blocks.LAVA.getDefaultState());
					}
					LavaDynamics.volcanoGen = false;
					return;
				}
			}
		}
		return;
	}

}
