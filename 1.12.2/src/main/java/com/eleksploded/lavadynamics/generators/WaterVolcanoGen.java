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
import net.minecraft.world.gen.feature.WorldGenerator;

public class WaterVolcanoGen extends WorldGenerator {
	
	List<IBlockState> ores = new ArrayList<IBlockState>();
	
	//Credit to CplPibald#7182 on discord for pointing out possible performance issue
	@SuppressWarnings("deprecation")
	public WaterVolcanoGen() {
		super(!LavaConfig.volcano.worldGen);
		System.out.print("Test");
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
	public boolean generate(World world, Random rand, BlockPos position) {
		
		if(ArrayUtils.contains(LavaConfig.volcano.validDimensions, world.provider.getDimension())){ return false; }
		rand = new Random();
		
		boolean full = false;
		if(rand.nextInt(100) + 1 <= LavaConfig.volcano.waterVolcanoChance){
			full = true;
		}
		int height = rand.nextInt(LavaConfig.volcano.volcanoHeightMax-LavaConfig.volcano.volcanoHeightMin+1) + LavaConfig.volcano.volcanoHeightMin;
		if(height <= 0) {
			return false;
		}
		
		System.out.println(height);
		
		int caldera = (rand.nextInt(LavaConfig.volcano.calderaMax-LavaConfig.volcano.calderaMin+1) + LavaConfig.volcano.calderaMin);
		
		BlockPos pos = position.up(height);
		
		int i = caldera;
		int j = pos.getY();
		while(full) {
			BlockPos pos1 = new BlockPos(pos.getX(),j,pos.getZ());
			if(isCornerWater(world,pos1,i)) {
				circle(i,world,pos1,j);
				setBlockWithOre(world, pos1);
				i = i+1;
				j = j-1;
			} else {
				break;
			}
		}
		
		BlockPos fill1 = position.up(height);
		while(world.getBlockState(fill1).getBlock() == Blocks.WATER){
			
			fill1=fill1.down();
			if(world.getBlockState(fill1).getBlock() != Blocks.WATER){
				break;
			}
			if(fill1.getY()<=0){
				break;
			}
		}
		float h = (float)height;
		fill1=fill1.up(Math.round(h/2));
		
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

	private boolean isCornerWater(World world, BlockPos pos, int radius) {
		if(world.getBlockState(pos.east(radius)).getBlock() == Blocks.WATER){
			return true;
		} else if(world.getBlockState(pos.west(radius)).getBlock() == Blocks.WATER){
			return true;
		} else if(world.getBlockState(pos.north(radius)).getBlock() == Blocks.WATER){
			return true;
		} else if(world.getBlockState(pos.south(radius)).getBlock() == Blocks.WATER){
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

		IBlockState block;
		int ore = rand.nextInt(1000);
		int chance = 1000-LavaConfig.volcano.oreChance;

		if(ore <= chance) {
			block=Blocks.STONE.getDefaultState();
		} else {
			block=ores.get(rand.nextInt(ores.size()));
		}

		this.setBlockAndNotifyAdequately(worldIn, blockpos, block);

	}
}
