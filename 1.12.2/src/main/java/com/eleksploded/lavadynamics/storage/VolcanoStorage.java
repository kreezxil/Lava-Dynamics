package com.eleksploded.lavadynamics.storage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.eleksploded.lavadynamics.LavaConfig;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;

public class VolcanoStorage {
	List<Chunk> chunks = new CopyOnWriteArrayList<Chunk>();
	Map<Chunk,Integer> tops = new HashMap<Chunk,Integer>();
	String fileName = "LD_VolcanoStorage";
	int dimID;
	
	public VolcanoStorage(int dimid){
		dimID = dimid;
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
					Chunk chunk = event.getWorld().getChunkFromChunkCoords(Integer.valueOf(tmp[0]), Integer.valueOf(tmp[1]));
					chunks.add(chunk);
					tops.put(chunk, Integer.valueOf(tmp[2]));
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
					String tmp = chunk.x + "|" + chunk.z + "|" + tops.get(chunk);
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
		if(!chunks.isEmpty()){
			return chunks.contains(chunk);
		} else {
			return false;
		}
	}
	
	public int getTop(Chunk chunk){
		if(tops.containsKey(chunk)){
			return tops.get(chunk);
		} else {
			return 0;
		}
	}
	
	public void addVolcano(Chunk chunk, int top) {
		chunks.add(chunk);
		tops.put(chunk, top);
	}
	
	public boolean isVolcanoInRange(Chunk chunk){		
		int xIn = (chunk.getPos().getXEnd() - chunk.getPos().getXStart())/2 + chunk.getPos().getXStart();
		int zIn = (chunk.getPos().getZEnd() - chunk.getPos().getZStart())/2 + chunk.getPos().getZStart();	
		
		if(!LavaConfig.volcano.spawnChunks) {
			BlockPos spawnpos = new BlockPos(chunk.getWorld().getSpawnPoint().getX(), 70, chunk.getWorld().getSpawnPoint().getZ());
			if(spawnpos.getDistance(xIn, 70, zIn) <= LavaConfig.volcano.spawnDistance) {
				return true;
			}	
		}
		
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
	
	public List<Chunk> getList() {
		return chunks;
	}
}
