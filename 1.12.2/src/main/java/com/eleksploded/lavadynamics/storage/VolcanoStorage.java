package com.eleksploded.lavadynamics.storage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.eleksploded.lavadynamics.LavaConfig;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;

public class VolcanoStorage {
	List<Chunk> chunks = new ArrayList<Chunk>();
	String fileName = "LD_VolcanoStorage";
	int dimID;
	
	public VolcanoStorage(int dimid){
		dimID = dimid;
	}

	File getFile() {
		String tmp = DimensionManager.getCurrentSaveRootDirectory() + "/";
<<<<<<< HEAD
		if(DimensionManager.getProvider(dimID).getSaveFolder() != null) {
			tmp = tmp + DimensionManager.getProvider(dimID).getSaveFolder() + "/" + fileName;
=======
		if(DimensionManager.createProviderFor(dimID).getSaveFolder() != null) {
			tmp = tmp + DimensionManager.createProviderFor(dimID).getSaveFolder() + "/" + fileName;
>>>>>>> 1.12.2-fixed
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
			throw new RuntimeException("Error loading VolcanoStorage. Please report this on the github page.");
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
			throw new RuntimeException("Error saving VolcanoStorage. Please report this on the github page.");
		}
	}
	
	public boolean isVolcano(Chunk chunk) {
		return chunks.contains(chunk);
	}
	
	public void addVolcano(Chunk chunk) {
		chunks.add(chunk);
	}
	
	public boolean isVolcanoInRange(Chunk chunk){
		int xIn = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int zIn = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();	
		
		for(Chunk chunkIn : chunks){
			int x = (chunkIn.getPos().getXEnd() - chunkIn.getPos().getXStart())/2 + chunkIn.getPos().getXStart();
			int z = (chunkIn.getPos().getZEnd() - chunkIn.getPos().getZStart())/2 + chunkIn.getPos().getZStart();	
			BlockPos pos = new BlockPos(x,70,z);
			
			if(pos.getDistance(xIn, 70, zIn) <= LavaConfig.volcano.distance){
				return true;
			}
		}
		return false;
	}
}
