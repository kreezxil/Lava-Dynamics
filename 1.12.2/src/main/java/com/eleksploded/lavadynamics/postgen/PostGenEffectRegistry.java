package com.eleksploded.lavadynamics.postgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import com.eleksploded.lavadynamics.LavaConfig;

import net.minecraft.world.chunk.Chunk;

public class PostGenEffectRegistry {
	static List<IPostGenEffect> effects = new ArrayList<IPostGenEffect>();
	
	public static void registerEffect(IPostGenEffect in){
		effects.add(in);
	}
	
	/**
	 * Returns the effect by name, null if no effects match
	 * @param name
	 * @return PostGenEffect by name
	 */
	public static IPostGenEffect getByName(String name){
		for(IPostGenEffect effect : effects){
			if(effect.getName().equalsIgnoreCase(name)){
				return effect;
			}
		}
		return null;
	}
	
	public static void runEffect(Chunk chunk, int top){
		Random rand = new Random();
		int index = rand.nextInt(effects.size());
		if(!ArrayUtils.contains(LavaConfig.postgen.effectBlacklist, effects.get(index))){
			effects.get(index).execute(chunk, top);
		}
	}
	
	/**
	 * Returns a list of all effects register by name
	 * @return List<String> of all effect names
	 */
	public static List<String> getAllNames(){
		List<String> list = new ArrayList<String>();
		for(IPostGenEffect effect : effects){
			list.add(effect.getName());
		}
		return list;
	}
}
