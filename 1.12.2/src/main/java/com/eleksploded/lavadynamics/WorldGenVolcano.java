package com.eleksploded.lavadynamics;

import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenVolcano implements IWorldGenerator {
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		Random random = new Random();
		if(random.nextInt(100)+1 <= LavaConfig.volcano.volcanoChance && LavaConfig.volcano.worldGen && ArrayUtils.contains(LavaConfig.volcano.validDimensions, world.provider.getDimension())) {
			Volcano.genVolcano(world.getChunkFromChunkCoords(chunkX, chunkZ), world);
		}
	}
}
