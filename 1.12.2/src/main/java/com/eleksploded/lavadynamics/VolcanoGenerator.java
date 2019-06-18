package com.eleksploded.lavadynamics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class VolcanoGenerator extends WorldGenerator {

	public VolcanoGenerator() {
		//Be sure to update blocks around it, since we are after worldgen
		super(true);
	}

	public boolean generate(World world, Random rand, BlockPos position)
	{
		while (world.isAirBlock(position) && position.getY() > 2)
		{
			position = position.down();
		}

		int height = (rand.nextInt((Config.volcanoHeightDeviation*2)+1)-Config.volcanoHeightDeviation) + Config.volcanoHeightBase;
		if(height <= 0) {
			return false;
		}
		
		int caldera = (rand.nextInt((Config.calderadeviation*2)+1)-Config.calderadeviation) + Config.calderaradius;
		
		BlockPos pos = position.up(height);
		System.out.println(pos);
		
		int i = caldera;
		int j = pos.getY();
		while(true) {
			BlockPos pos1 = new BlockPos(pos.getX(),j,pos.getZ());
			if(isCornerAir(world,pos1,i)) {
				System.out.println("In Loop " + pos1 + " with " + i);
				circle(i,world,pos1,j);
				setBlockWithOre(world, pos1);
				i = i+1;
				j = j-1;
			} else {
				break;
			}
		}
		
		BlockPos fill1 = pos.down(caldera-2);
		for(int radius = caldera-1;radius != 0;radius--){
			int x1 = fill1.getX();
			int y1 = fill1.getY()-1;
			int z1 = fill1.getZ();
			
			for(float i1 = 0; i1 < radius; i1 += 0.5) {
				for(float j1 = 0; j1 < 2 * Math.PI * i1; j1 += 0.5)
					world.setBlockState(new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1 + radius, (int)Math.floor(z1 + Math.cos(j1) * i1)), Blocks.LAVA.getDefaultState());
			}
			fill1 = fill1.down();
		}

		return true;
	}

	private boolean isCornerAir(World world, BlockPos pos, int radius) {
		if(world.getBlockState(pos.east(radius)).getBlock() == Blocks.AIR){
			return true;
		} else if(world.getBlockState(pos.west(radius)).getBlock() == Blocks.AIR){
			return true;
		} else if(world.getBlockState(pos.north(radius)).getBlock() == Blocks.AIR){
			return true;
		} else if(world.getBlockState(pos.south(radius)).getBlock() == Blocks.AIR){
			return true;
		} else {
			return false;
		}
	}

	public void circle(int radius, World world, BlockPos fill1, int j) {
		int x1 = fill1.getX();
		int y1 = fill1.getY();
		int z1 = fill1.getZ();
		
		System.out.println("Center of Circle: " + new BlockPos(x1,y1,z1));

		for(float i1 = 0; i1 < radius; i1 += 0.5) {
			for(float j1 = 0; j1 < 2 * Math.PI * i1; j1 += 0.5)
				setBlockWithOre(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)));
		}
	}

	private void setBlockWithOre(World worldIn, BlockPos blockpos) {
		Random rand = new Random();

		Block block;
		int ore = rand.nextInt(1000);
		int chance = 1000-Config.oreChance;

		if(ore <= chance) {
			block=Blocks.STONE;
		} else {
			block=genRandOre(rand);
		}

		this.setBlockAndNotifyAdequately(worldIn, blockpos, block.getDefaultState());

	}

	private Block genRandOre(Random rand) {
		List<Block> list = new ArrayList<Block>();
		
		List<String> names = Arrays.stream(Config.ores).collect(Collectors.toList());
		
		for(String name : names) {
			Block block = Block.getBlockFromName(name);
			int chance = Config.chance[names.indexOf(name)];
			for(int i = 0;i != chance;i++){
				list.add(block);
			}
		}
				
		return list.get(rand.nextInt(list.size()));
	}


}
