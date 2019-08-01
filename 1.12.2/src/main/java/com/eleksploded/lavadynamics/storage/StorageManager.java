package com.eleksploded.lavadynamics.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import com.eleksploded.lavadynamics.LavaConfig;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StorageManager {
	public static boolean loaded = false;
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
		loaded = true;
	}
	 
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	static void load(WorldEvent.Load event){
		
		for(Entry<Integer, CheckedStorage> entry : checked.entrySet()){
			entry.getValue().load(event);
		}
		for(Entry<Integer, VolcanoStorage> entry : volcano.entrySet()){
			entry.getValue().load(event);
		}
	}
	
	@SubscribeEvent
	static void save(WorldEvent.Save event){
		for(Entry<Integer, CheckedStorage> entry : checked.entrySet()){
			entry.getValue().save(event);
		}
		for(Entry<Integer, VolcanoStorage> entry : volcano.entrySet()){
			entry.getValue().save(event);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	static void unload(WorldEvent.Unload event){
		loaded = false;
	}
}
