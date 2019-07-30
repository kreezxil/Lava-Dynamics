package com.eleksploded.lavadynamics.storage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;

public class CheckedStorage {
	List<Chunk> chunks = new CopyOnWriteArrayList<Chunk>();
	String fileName = "LD_CheckedStorage";
	int dimID;
	
	public CheckedStorage(int dimIDin){
		dimID = dimIDin;
	}
	
	File getFile() {
		String tmp = DimensionManager.getCurrentSaveRootDirectory() + "/";

		if(DimensionManager.getProvider(dimID).getSaveFolder() != null) {
			tmp = tmp + DimensionManager.getProvider(dimID).getSaveFolder() + "/" + fileName;
		} else {
			tmp = tmp + fileName;
		}
		return new File(tmp);
	}
	
	public void load(WorldEvent.Load event){
		if(event.getWorld().provider.getDimension() != dimID){ return; }
		
		try{
			if(getFile().exists()){
				Path path = getFile().toPath();
				for(String in : Files.readAllLines(path)){
					String[] tmp = in.split("\\|");
					chunks.add(event.getWorld().getChunkFromChunkCoords(Integer.valueOf(tmp[0]), Integer.valueOf(tmp[1])));
				}
			} else {
				getFile().createNewFile();
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error loading CheckedStorage. Please report this on the github page.");
		}
	}
	
	public void save(WorldEvent.Save event){
		if(event.getWorld().provider.getDimension() != dimID){ return; }
		
		try{
			if(getFile().exists()){
				Path path = getFile().toPath();
				List<String> list = new ArrayList<String>();
				for(Chunk chunk : chunks){
					String tmp = chunk.x + "|" + chunk.z;
					if(!list.contains(tmp)){
						list.add(tmp);
					}
					getFile().delete();
					getFile().createNewFile();
					Files.write(path, list);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error saving CheckedStorage. Please report this on the github page.");
		}
	}
	
	public boolean isChecked(Chunk chunk) {
		return chunks.contains(chunk);
	}
	
	public void addChecked(Chunk chunk) {
		if(!chunks.contains(chunk)) {
			chunks.add(chunk);
		}
	}
}