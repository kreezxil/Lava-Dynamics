package com.eleksploded.lavadynamics.storage;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.eleksploded.lavadynamics.LavaConfig;

import net.minecraftforge.common.DimensionManager;

public class StorageManager {
	static Map<Integer, CheckedStorage> checked = new HashMap<Integer,CheckedStorage>();
	static Map<Integer, VolcanoStorage> volcano = new HashMap<Integer,VolcanoStorage>();
	
	public static CheckedStorage getCheckedStorage(int DimID){
		return checked.get(DimID);
	}
	
	public static VolcanoStorage getVolcanoStorage(int DimID){
		return volcano.get(DimID);
	}
	
	public static void init(){
		for(int id : DimensionManager.getStaticDimensionIDs()){
			if(ArrayUtils.contains(LavaConfig.volcano.validDimensions, id)){
				checked.put(id, new CheckedStorage(id));
				volcano.put(id, new VolcanoStorage(id));
			}
		}
	}
}
