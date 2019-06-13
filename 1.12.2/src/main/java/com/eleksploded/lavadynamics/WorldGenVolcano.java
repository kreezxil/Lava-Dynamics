package com.eleksploded.lavadynamics;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenVolcano implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if(random.nextInt(100)+1 <= Config.volcanoChance && Config.worldGen) {
			Volcano.genVolcano(world.getChunkFromChunkCoords(chunkX, chunkZ), world);
		}
	}
}
