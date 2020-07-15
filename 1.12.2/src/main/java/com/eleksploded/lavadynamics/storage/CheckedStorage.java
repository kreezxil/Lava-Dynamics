package com.eleksploded.lavadynamics.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;

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
		File file =  new File(tmp);

		try {
			if(!file.exists()) file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating CheckedStorage. Please report this on the github page.");
		}
		
		return file;
	}

	public boolean isChecked(Chunk c) {
		try {
			String chunk = c.x + "|" + c.z;
			BufferedReader r = Files.newReader(getFile(), StandardCharsets.UTF_8);
			String line;
			while((line = r.readLine()) != null) {
				if(line.contentEquals(chunk)) {
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error checking CheckedStorage. Please report this on the github page.");
		}
	}

	public void addChecked(Chunk chunk) {
		if(!chunks.contains(chunk)) {
			String toWrite = "\r\n" + chunk.x + "|" + chunk.z;

			try {
				FileUtils.writeStringToFile(getFile(), toWrite, StandardCharsets.UTF_8, true);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Error creating CheckedStorage. Please report this on the github page.");
			}
		}
	}
}