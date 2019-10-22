package com.eleksploded.lavadynamics.postgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import com.eleksploded.lavadynamics.LavaConfig;

import net.minecraft.world.chunk.Chunk;

public class PostGenEffectRegistry {
	static Map<String,IPostGenEffect> effects = new HashMap<String,IPostGenEffect>();
	
	/**
	 * Register an effect
	 * @param in
	 */
	public static void registerEffect(IPostGenEffect in){
		effects.put(in.getName(), in);
	}
	
	/**
	 * Returns the effect by name, null if no effects match
	 * @param name
	 * @return PostGenEffect by name
	 */
	public static IPostGenEffect getByName(String name){
		for(Entry<String, IPostGenEffect> effect : effects.entrySet()){
			if(effect.getKey().equalsIgnoreCase(name)){
				return effect.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Runs a random effect
	 * @param chunk
	 * @param top
	 */
	public static void runEffect(Chunk chunk, int top){
		Random rand = new Random();
		int index = rand.nextInt(effects.keySet().size());
		List<String> list = new ArrayList<String>(effects.keySet());
		if(!ArrayUtils.contains(LavaConfig.postgen.effectBlacklist, list.get(index))) {
			effects.get(list.get(index)).execute(chunk, top);
		}
	}
	
	/**
	 * Returns a list of all effects register by name
	 * @return List<String> of all effect names
	 */
	public static List<String> getAllNames(){
		return new ArrayList<String>(effects.keySet());
	}
}
