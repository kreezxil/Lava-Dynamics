package com.eleksploded.ldcompathelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ErrorCheckerThread extends Thread {
	public boolean error = false;
	public World world;
	File worldDir;
	
	public ErrorCheckerThread(World worldIn){
		world = worldIn;
	}

	public void run() {
		while(!error){
			worldDir = DimensionManager.getCurrentSaveRootDirectory() == null ? worldDir : DimensionManager.getCurrentSaveRootDirectory();
			
			if(world.isRemote){
				//System.out.println("Client");
				break;
			} else {
				//System.out.println("Server");
			}

			try{
				if(Minecraft.getMinecraft().getIntegratedServer() == null) {
					onError();
				}
			} catch (java.lang.NoSuchMethodError e) {
				
			}
			if(error){
				break;
			}
		}
	}

	public void onError() {
		error = true;
		System.out.println("Crash Detected");
		File file = new File(worldDir + "/" + "LDCompat_FileTest");
		try{
			file.createNewFile();
			Files.write(file.toPath(), buildList());

		} catch(Exception e){
			
		}
		try {
			Set<PosixFilePermission> perms = Files.getPosixFilePermissions(file.toPath());
			for(PosixFilePermission perm : perms){
				System.out.println(perm.toString());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private List<String> buildList() {
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < 150; i++){
			list.add(String.valueOf(i));
		}
		return list;
	}
}
