package com.eleksploded.lavadynamics.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.eleksploded.lavadynamics.LavaConfig;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class ConeVolcanoGen extends WorldGenerator {

	public ConeVolcanoGen() {
		//Be sure to update blocks around it, since we are after worldgen
		super(true);
	}

	public boolean generate(World world, Random random, BlockPos position)
	{
		//if(ArrayUtils.contains(LavaConfig.volcano.validDimensions, world.provider.getDimension())){ return false; }
		
		Random rand = new Random();
		while (world.isAirBlock(position) && position.getY() > 15)
		{
			position = position.down();
			System.out.println(position.getY());
		}
		
		int height = rand.nextInt(LavaConfig.volcano.volcanoHeightMax-LavaConfig.volcano.volcanoHeightMin+1) + LavaConfig.volcano.volcanoHeightMin;
		if(height <= 0) {
			return false;
		}
		
		int caldera = (rand.nextInt(LavaConfig.volcano.calderaMax-LavaConfig.volcano.calderaMin+1) + LavaConfig.volcano.calderaMin);
		
		BlockPos pos = position.up(height);
		
		int i = caldera;
		int j = pos.getY();
		while(true) {
			BlockPos pos1 = new BlockPos(pos.getX(),j,pos.getZ());
			if(isCornerAir(world,pos1,i)) {
				circle(i,world,pos1,j);
				setBlockWithOre(world, pos1);
				i = i+1;
				j = j-1;
			} else {
				break;
			}
		}
		
		BlockPos fill1 = position.up(height);
		while(!world.isAirBlock(fill1)){
			fill1=fill1.down();
			if(world.isAirBlock(fill1)){
				break;
			}
			if(fill1.getY()<=0){
				break;
			}
		}
		
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
		
		for(float i1 = 0; i1 < radius; i1 += 0.5) {
			for(float j1 = 0; j1 < 2 * Math.PI * i1; j1 += 0.5)
				setBlockWithOre(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)));
		}
	}

	private void setBlockWithOre(World worldIn, BlockPos blockpos) {
		Random rand = new Random();

		Block block;
		int ore = rand.nextInt(1000);
		int chance = 1000-LavaConfig.volcano.oreChance;

		if(ore <= chance) {
			block=Blocks.STONE;
		} else {
			block=genRandOre(rand);
		}

		this.setBlockAndNotifyAdequately(worldIn, blockpos, block.getDefaultState());

	}

	private Block genRandOre(Random rand) {
		List<Block> list = new ArrayList<Block>();
		
		List<String> names = Arrays.stream(LavaConfig.volcano.ores).collect(Collectors.toList());
		
		for(String name : names) {
			Block block = Block.getBlockFromName(name);
			int chance = LavaConfig.volcano.chance[names.indexOf(name)];
			for(int i = 0;i != chance;i++){
				list.add(block);
			}
		}
				
		return list.get(rand.nextInt(list.size()));
	}


}
