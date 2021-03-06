package com.eleksploded.lavadynamics.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.eleksploded.lavadynamics.LavaConfig;
import com.eleksploded.lavadynamics.LavaDynamics;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

public class ConeVolcanoGen extends WorldGenerator {

	List<IBlockState> ores = new ArrayList<IBlockState>();

	//Credit to CplPibald#7182 on discord for pointing out possible performance issue
	@SuppressWarnings("deprecation")
	public ConeVolcanoGen() {
		super(!LavaConfig.volcano.worldGen);
		List<String> names = Arrays.stream(LavaConfig.volcano.ores).collect(Collectors.toList());

		if(names.size() != LavaConfig.volcano.chance.length) {
			LavaDynamics.Logger.error("Detected Invalid Config: Ores do not match OreChance in config. Disabling ore generation in volcanoes");
			names.clear();
		}

		for(String name : names) {
			String[] split = name.split("\\|");
			if(split.length != 2){
				LavaDynamics.Logger.error("Skipping invalid Config at " + name);
				continue;
			}
			IBlockState block = Blocks.STONE.getDefaultState();
			try{
				block = Block.getBlockFromName(split[0]).getStateFromMeta(Integer.valueOf(split[1]));
			} catch(NumberFormatException | NullPointerException e) {
				LavaDynamics.Logger.error("Skipping invalid Config at " + name);
				continue;
			}
			int chance = LavaConfig.volcano.chance[names.indexOf(name)];
			for(int i = 0;i != chance;i++){
				ores.add(block);
			}
		}
	}

	public boolean generate(World world, Random random, BlockPos position)
	{

		Random rand = new Random();
		while (world.isAirBlock(position) && position.getY() > 15)
		{
			position = position.down();
			//Cap at world limits, hopefully this should never happen though
			if(position.getY() >= 255 || position.getY() <= 16) {
				break;
			}
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

			if(j >= 255 || j <= 3) {
				break;
			}

			BlockPos pos1 = new BlockPos(pos.getX(),j,pos.getZ());
			if(isCornerAir(world,pos1,i) || pos1.getY() >= 255 || pos.getY() <= 3) {
				circle(i,world,pos1,j);
				setBlockWithOre(world, pos1, false);
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
			for(float j1 = 0; j1 < 2 * Math.PI * i1; j1 += 0.5) {
				if(LavaConfig.volcano.useBiome && i1 == radius-.5 && radius > LavaConfig.volcano.biomeStart) {
					if(radius == LavaConfig.volcano.biomeStart && world.rand.nextBoolean()) {
						setBlockWithOre(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)), false);
					} else {
						setBlockWithBiomeTop(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)));
					}
				} else {
					setBlockWithOre(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)), LavaConfig.volcano.useBiome && i1 >= (radius-((LavaConfig.volcano.fillerSize) + .5F)));
				}
			}
		}
	}

	private void setBlockWithOre(World worldIn, BlockPos blockpos, boolean useFiller) {
		Random rand = new Random();

		IBlockState block;
		int ore = rand.nextInt(1000);
		int chance = 1000-LavaConfig.volcano.oreChance;

		if(ore <= chance) {
			if(useFiller) {
				block = worldIn.getBiome(blockpos).fillerBlock;
			} else {
				block=Blocks.STONE.getDefaultState();
			}
		} else {
			block= ores.get(rand.nextInt(ores.size()));
		}

		this.setBlockAndNotifyAdequately(worldIn, blockpos, block);
	}

	private void setBlockWithBiomeTop(World world, BlockPos blockpos) {
		Biome biome = world.getBiome(blockpos);

		this.setBlockAndNotifyAdequately(world, blockpos, biome.topBlock);
	}
}
