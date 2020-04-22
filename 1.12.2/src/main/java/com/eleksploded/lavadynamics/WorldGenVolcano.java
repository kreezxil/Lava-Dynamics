package com.eleksploded.lavadynamics;

import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import com.eleksploded.lavadynamics.storage.CheckedStorage;
import com.eleksploded.lavadynamics.storage.StorageManager;
import com.eleksploded.lavadynamics.storage.VolcanoStorage;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenVolcano implements IWorldGenerator {
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if(LavaConfig.volcano.worldGen && rand.nextInt(100)+1 <= LavaConfig.volcano.volcanoChance &&  ArrayUtils.contains(LavaConfig.volcano.validDimensions, world.provider.getDimension())) {
			
			VolcanoStorage store = StorageManager.getVolcanoStorage(world.provider.getDimension());
			CheckedStorage check = StorageManager.getCheckedStorage(world.provider.getDimension());
			chunkGenerator.populate(chunkX, chunkZ);
			Chunk chunk = chunkProvider.provideChunk(chunkX, chunkZ);
			
			if(!check.isChecked(chunk)) {
				check.addChecked(chunk);
				if(!store.isVolcano(chunk) && store.isVolcanoInRange(chunk)) {
					Volcano.genVolcano(chunk, world);
				}
			}
		}
	}
}
