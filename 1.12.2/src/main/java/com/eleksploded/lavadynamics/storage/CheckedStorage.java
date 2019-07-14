package com.eleksploded.lavadynamics.storage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.eleksploded.lavadynamics.LavaConfig;
import com.eleksploded.lavadynamics.Reference;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class CheckedStorage {
	static List<Chunk> chunks = new ArrayList<Chunk>();
	static String fileName = "LD_CheckedStorage";

	static File getFile(World world) {
		String tmp = DimensionManager.getCurrentSaveRootDirectory() + "/";
		if(world.provider.getSaveFolder() != null) {
			tmp = tmp + world.provider.getSaveFolder() + "/" + fileName;
		} else {
			tmp = tmp + fileName;
		}
		return new File(tmp);
	}
	
	@SubscribeEvent
	static void load(WorldEvent.Load event){
		try{
			if(getFile(event.getWorld()).exists()){
				Path path = getFile(event.getWorld()).toPath();
				for(String in : Files.readAllLines(path)){
					String[] tmp = in.split("\\|");
					chunks.add(event.getWorld().getChunkFromChunkCoords(Integer.valueOf(tmp[0]), Integer.valueOf(tmp[1])));
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error loading CheckedStorage. Please report this on the github page.");
		}
	}
	
	@SubscribeEvent
	static void save(WorldEvent.Save event){
		try{
			if(getFile(event.getWorld()).exists()){
				Path path = getFile(event.getWorld()).toPath();
				List<String> list = new ArrayList<String>();
				for(Chunk chunk : chunks){
					String tmp = chunk.x + "|" + chunk.z;
					if(!list.contains(tmp)){
						list.add(tmp);
					}
					Files.write(path, list);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error saving CheckedStorage. Please report this on the github page.");
		}
	}
	
	public static boolean isChecked(Chunk chunk) {
		return chunks.contains(chunk);
	}
	
	public static void addChecked(Chunk chunk) {
		chunks.add(chunk);
	}
}