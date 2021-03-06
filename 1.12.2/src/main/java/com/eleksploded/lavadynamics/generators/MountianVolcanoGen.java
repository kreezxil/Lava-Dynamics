package com.eleksploded.lavadynamics.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.eleksploded.lavadynamics.LavaConfig;
import com.eleksploded.lavadynamics.LavaDynamics;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

public class MountianVolcanoGen extends WorldGenerator {

	List<IBlockState> ores = new ArrayList<IBlockState>();
	
	//Credit to CplPibald#7182 on discord for pointing out possible performance issue
	@SuppressWarnings("deprecation")
	public MountianVolcanoGen() {
		super(!LavaConfig.volcano.worldGen);
		
		List<String> names = Arrays.stream(LavaConfig.volcano.ores).collect(Collectors.toList());
		
		if(names.size() != LavaConfig.volcano.chance.length) {
			LavaDynamics.Logger.error("Detected Invalid Config: Ores do not match OreChance in config. Disabling ore generation in volcanoes");
			names.clear();
		}
		
		for(String name : names) {
			
			String[] tmp = name.split("\\|");
			if(tmp.length != 2){
				LavaDynamics.Logger.error("Skipping invalid Config at " + name);
				continue;
			}
			IBlockState block = Blocks.STONE.getDefaultState();
			try{
				block = Block.getBlockFromName(tmp[0]).getStateFromMeta(Integer.valueOf(tmp[1]));
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
	
	@Override
	public boolean generate(World world, Random random, BlockPos position) {
		
		if(ArrayUtils.contains(LavaConfig.volcano.validDimensions, world.provider.getDimension())){ return false; }
		
		Random rand = new Random();
		while (world.isAirBlock(position) && position.getY() > 2)
		{
			position = position.down();
			//Cap at world limits, hopefully this should never happen though
			if(position.getY() >= 255 || position.getY() <= 16) {
				break;
			}
		}
		//System.out.println("Mountian");
		int caldera = (rand.nextInt(LavaConfig.volcano.calderaMax-LavaConfig.volcano.calderaMin+1) + LavaConfig.volcano.calderaMin);
		BlockPos pos = position;
		
		for(int radius = caldera; radius != 0; radius--){
			int x1 = pos.getX();
			int y1 = pos.getY()-1;
			int z1 = pos.getZ();
			
			for(float i1 = 0; i1 < radius; i1 += 0.5) {
				for(float j1 = 0; j1 < 2 * Math.PI * i1; j1 += 0.5)
					setBlockWithOre(world, new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1 + radius, (int)Math.floor(z1 + Math.cos(j1) * i1)), false);
			}
			pos = pos.down();
		}
		
		for(int radius = caldera-1; radius != 0; radius--){
			int x1 = position.getX();
			int y1 = position.getY()-1;
			int z1 = position.getZ();
			
			for(float i1 = 0; i1 < radius; i1 += 0.5) {
				for(float j1 = 0; j1 < 2 * Math.PI * i1; j1 += 0.5)
					if(LavaConfig.volcano.useBiome && i1 == radius-.5) {
						setBlockWithBiomeTop(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)));
					} else {
						setBlockWithOre(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)), LavaConfig.volcano.useBiome && i1 >= (radius-((LavaConfig.volcano.fillerSize) + .5F)));
					}
			}
			position = position.down();
		}
		
		return true;
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
