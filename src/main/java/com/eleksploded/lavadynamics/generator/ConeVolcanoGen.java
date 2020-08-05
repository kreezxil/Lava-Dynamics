package com.eleksploded.lavadynamics.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.eleksploded.lavadynamics.LavaDynamics;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class ConeVolcanoGen implements IVolcanoGenerator {

	List<BlockState> ores = new ArrayList<BlockState>();

	//Credit to CplPibald#7182 on discord for pointing out possible performance issue
	public ConeVolcanoGen() {
		
		//TODO: Error in here somewhere. Too tired to figure it out now
		
		List<String> names = Arrays.stream((String[]) LavaDynamics.LavaConfig.getValue("ores")).collect(Collectors.toList());
		
		for(String name : names) {
			String[] split = name.split("\\|");
			if(split.length != 2){
				LavaDynamics.Logger.error("Skipping invalid Config at " + name);
				continue;
			}
			BlockState block = Blocks.STONE.getDefaultState();
		
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(split[0]));
			
			if(b != null) {
				block = b.getDefaultState();
			}
			
			int chance = Integer.valueOf(split[1]);
			for(int i = 0;i != chance;i++){
				ores.add(block);
			}
		}
	}

	public void generate(World world, Random random, BlockPos position)
	{
		boolean debug = LavaDynamics.LavaConfig.getBool("debug");
		Random rand = new Random();

		if(debug) LavaDynamics.Logger.debug("Get Height for Volcano");
		int height = rand.nextInt(LavaDynamics.LavaConfig.getInt("heightMax")-LavaDynamics.LavaConfig.getInt("heightMin")+1) + LavaDynamics.LavaConfig.getInt("heightMin");
		if(height <= 0) {
			return;
		}

		if(debug) LavaDynamics.Logger.debug("Get Caldera for volcano");
		int caldera = (rand.nextInt(LavaDynamics.LavaConfig.getInt("calderaMax")-LavaDynamics.LavaConfig.getInt("calderaMin")+1) + LavaDynamics.LavaConfig.getInt("calderaMin"));

		BlockPos pos = position.up(height);

		int i = caldera;
		int j = pos.getY();
		if(debug) LavaDynamics.Logger.debug("Generate Volcano Cone");
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
		
		if(debug) LavaDynamics.Logger.debug("Offsetting for caldera");
		
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
		
		if(debug) LavaDynamics.Logger.debug("Filling caldera");
		
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
		if(debug) LavaDynamics.Logger.debug("Done with Generator");
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
				if(LavaDynamics.LavaConfig.getBool("useBiome") && i1 == radius-.5 && radius > LavaDynamics.LavaConfig.getInt("biomeStart")) {
					if(radius == LavaDynamics.LavaConfig.getInt("biomeStart") && world.rand.nextBoolean()) {
						setBlockWithOre(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)), false);
					} else {
						setBlockWithBiomeTop(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)));
					}
				} else {
					setBlockWithOre(world,new BlockPos((int)Math.floor(x1 + Math.sin(j1) * i1), y1, (int)Math.floor(z1 + Math.cos(j1) * i1)), LavaDynamics.LavaConfig.getBool("useBiome") && i1 >= (radius-((LavaDynamics.LavaConfig.getInt("fillerSize")) + .5F)));
				}
			}
		}
	}

	private void setBlockWithOre(World worldIn, BlockPos blockpos, boolean useFiller) {
		Random rand = new Random();

		BlockState block;
		int ore = rand.nextInt(1000);
		int chance = 1000-LavaDynamics.LavaConfig.getInt("oreChance");

		if(ore <= chance) {
			if(useFiller) {
				block = worldIn.getBiome(blockpos).getSurfaceBuilderConfig().getUnder();
			} else {
				block=Blocks.STONE.getDefaultState();
			}
		} else {
			block= ores.get(rand.nextInt(ores.size()));
		}
		worldIn.setBlockState(blockpos, block);
	}

	private void setBlockWithBiomeTop(World world, BlockPos blockpos) {
		Biome biome = world.getBiome(blockpos);
		BlockState s = biome.getSurfaceBuilderConfig().getTop();
		world.setBlockState(blockpos, s);
	}
}
