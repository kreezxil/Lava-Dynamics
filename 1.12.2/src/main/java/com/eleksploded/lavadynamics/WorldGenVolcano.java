package com.eleksploded.lavadynamics;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenVolcano implements IWorldGenerator {
	private int timer = LavaConfig.volcano.volcanoCooldown;

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		
		if(!LavaConfig.volcano.worldGen) return;
		throw new IllegalStateException("WorldGen generation is currently non-functional. Please disable in the config");
		/*Chunk chunk = chunkProvider.provideChunk(chunkX, chunkZ);

		if(timer != 0){
			timer = timer-1;
			try {
				StorageManager.getCheckedStorage(world.provider.getDimension()).addChecked(chunk);
			} catch (NullPointerException e) {
				return;
			}
			return;
		}
		timer = LavaConfig.volcano.volcanoCooldown;
		
		if(ArrayUtils.contains(LavaConfig.volcano.validDimensions, world.provider.getDimension()) && rand.nextInt(100)+1 <= LavaConfig.volcano.volcanoChance ) {
			
			VolcanoStorage store = StorageManager.getVolcanoStorage(world.provider.getDimension());
			
			if(!StorageManager.getCheckedStorage(world.provider.getDimension()).isChecked(chunk)) {
				if(!store.isVolcano(chunk) && !store.isVolcanoInRange(chunk)) {
					Volcano.genVolcano(chunk, world);
				}
			}
		}*/
	}
}
