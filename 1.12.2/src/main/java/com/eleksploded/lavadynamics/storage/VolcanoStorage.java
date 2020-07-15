package com.eleksploded.lavadynamics.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.eleksploded.lavadynamics.LavaConfig;
import com.google.common.io.Files;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;

public class VolcanoStorage {
	String fileName = "LD_VolcanoStorage";
	int dimID;

	public VolcanoStorage(int dimid){
		dimID = dimid;

		if(!getFile().exists()) {
			try {
				getFile().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Error creating VolcanoStorage. Please report this on the github page.");
			}
		}
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



	public boolean isVolcano(Chunk c) {
		try {
			String chunk = c.x + "|" + c.z + "|";
			BufferedReader r = Files.newReader(getFile(), StandardCharsets.UTF_8);
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith(chunk)) {
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error checking VolcanoStorage. Please report this on the github page.");
		}
	}

	@SuppressWarnings("null")
	@Nullable
	public int getTop(Chunk c){
		try {
			String chunk = c.x + "|" + c.z + "|";
			BufferedReader r = Files.newReader(getFile(), StandardCharsets.UTF_8);
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith(chunk)) {
					String s[] = line.split("\\|");
					try {
						return Integer.valueOf(s[2]);
					} catch(NumberFormatException e) {
						e.printStackTrace();
						throw new RuntimeException("Error reading VolcanoStorage. Please report this on the github page.");
					}
				}
			}
			return (Integer) null;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error checking VolcanoStorage. Please report this on the github page.");
		}
	}

	public void addVolcano(Chunk chunk, int top) {	
		String toWrite = "\r\n" + chunk.x + "|" + chunk.z + "|" + top;

		try {
			FileUtils.writeStringToFile(getFile(), toWrite, StandardCharsets.UTF_8, true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating VolcanoStorage. Please report this on the github page.");
		}
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

		try {
			BufferedReader r = Files.newReader(getFile(), StandardCharsets.UTF_8);
			String line;
			while((line = r.readLine()) != null) {
				String s[] = line.split("\\|");
				Chunk chunkIn;
				try {
					chunkIn = chunk.getWorld().getChunk(Integer.valueOf(s[0]), Integer.valueOf(s[1]));
				} catch(NumberFormatException e) {
					e.printStackTrace();
					throw new RuntimeException("Error checking VolcanoStorage. Please report this on the github page.");
				}

				int x = (chunkIn.getPos().getXEnd() - chunkIn.getPos().getXStart())/2 + chunkIn.getPos().getXStart();
				int z = (chunkIn.getPos().getZEnd() - chunkIn.getPos().getZStart())/2 + chunkIn.getPos().getZStart();	
				BlockPos pos = new BlockPos(x,70,z);

				if(pos.getDistance(xIn, 70, zIn) <= LavaConfig.volcano.distance){
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error checking VolcanoStorage. Please report this on the github page.");
		}
		return false;
	}

	public Chunk get(int n, World world) {
		try {
			String line = "";
			BufferedReader r = Files.newReader(getFile(), StandardCharsets.UTF_8);
			for (int i = 0; i < n; i++) r.readLine();
			line = r.readLine();
			
			String s[] = line.split("\\|");
			try {
				return world.getChunk(Integer.valueOf(s[0]), Integer.valueOf(s[1]));
			} catch(NumberFormatException e) {
				e.printStackTrace();
				throw new RuntimeException("Error checking VolcanoStorage. Please report this on the github page.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error checking VolcanoStorage. Please report this on the github page.");
		}
	}

	public int getNum() {
		try {
			BufferedReader r = Files.newReader(getFile(), StandardCharsets.UTF_8);
			int lines = 0;
			while (r.readLine() != null) lines++;
			r.close();
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading VolcanoStorage. Please report this on the github page.");
		}

	}
}
