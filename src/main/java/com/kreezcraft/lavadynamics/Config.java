package com.kreezcraft.lavadynamics;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {

	public static Configuration cfg = LavaDynamics.config;

	public static String CATEGORY_GENERAL = "general", CATEGORY_DIMENSIONS = "dimensions", CATEGORY_CONVERSIONS = "conversions (mappings)", CVOLCANO = "Volcano Settings";

	public static Property debugMode, dimOverworld, dimNether, dimEnd, conversions, burping, furnaceRecipes, partialBlock, volcanoChance;

	public static Property dimensions; //a kludge for now
	
	public static Property volcanoGen,lowNoise,highNoise,maxExplosion,minExplosion,chanceExplosion,lavaSpread,psuedoSurface,extraHt,minHt,maxYlevel;

	// Call this from CommonProxy.preInit(). It will create our config if it doesn't
	// exist yet and read the values if it does exist.
	public static void readConfig() {
		try {
			cfg.load();
			initGeneralConfig(cfg);
		} catch (Exception e1) {
			LavaDynamics.logger.log(Level.ERROR, "Problem loading config file!", e1);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

	private static void initGeneralConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Settings");
		debugMode = cfg.get(CATEGORY_GENERAL, "debugMode", false,
				"If true it will print messages to the player based on what you are doing in the protected zone, useful for helping Kreezxil debug the mod");
		//burping = cfg.get(CATEGORY_GENERAL, "burping", false, "Allows lava to be burped to force block events to happen in it.");
		volcanoChance = cfg.get(CVOLCANO, "volcanoChance", 5, "Percent chance a volcano or magma vent will occur.\nDefault: 5");
		volcanoGen = cfg.get(CVOLCANO, "volcanoGen", false, "You can play with this value, but it's not for you.\nDefault: seriously it will ignore you");
		lowNoise = cfg.get(CVOLCANO, "lowNoise", 1, "The Minimum value for determining how many blocks above the current block is also lava.\nDefault: 1");
		highNoise = cfg.get(CVOLCANO, "highNoise", 4, "The Maximum value for determining how many blocks above the current block is also lava.\nDefault: 4");
	    maxExplosion = cfg.get(CVOLCANO, "maxExplosion", 10.0, "float value for determining strength of random explosions.\nDefault: 10.0");
	    minExplosion = cfg.get(CVOLCANO, "minExplosion", 1.0, "The minimum value for determing strenth of random explosions.\nDefault: 1.0");
	    chanceExplosion = cfg.get(CVOLCANO, "chanceExplosion", 5, "Integer value from 0 to 100 for determining the chance that lava causes an explosion.\nDefault: 5");
	    lavaSpread = cfg.get(CVOLCANO, "lavaSpread", 10, "The integer value from 0 to 100 for determing the chance that lava will spread.\nDefault: 10");
	    psuedoSurface = cfg.get(CVOLCANO, "psuedoSurface", 69, "The y level for the approximate level of your surface.\nDefault: 69");
	    extraHt = cfg.get(CVOLCANO, "extraHt", 10, "Extra amount of source blocks to add to the magma vent.\nDefault: 10");
	    minHt = cfg.get(CVOLCANO, "minHt", 3,"The minimum amount of source blocks to add to the magma vent.\nDefault: 3");
	    maxYlevel = cfg.get(CVOLCANO, "maxYlevel", 10, "The max y value to consider for turning lava into a magma vent.\nDefault: 10");
	    
//		cfg.addCustomCategoryComment(CATEGORY_DIMENSIONS, "Dimensions not listed are not allowed to function with Lava Dynamics. For instance don't list\n"+"Nether and you won't have a million blocks tyring to update their neighbors!");
//		dimensions = cfg.get(CATEGORY_DIMENSIONS, "dimensions", new int[]{0}, "List of dimension id's for which the mod is allowed to operate");
		
		cfg.addCustomCategoryComment(CATEGORY_DIMENSIONS, "True or False to control if LavaDynamics runs in said dimension");
		dimOverworld = cfg.get(CATEGORY_DIMENSIONS, "OverWorld",  true, "It's honestly meant to run here");
		dimNether = cfg.get(CATEGORY_DIMENSIONS, "Nether", false, "All that lava, probably you should allow it!");
		dimEnd = cfg.get(CATEGORY_DIMENSIONS, "The End", true, "I don't see why it can't run here");
		
		cfg.addCustomCategoryComment(CATEGORY_CONVERSIONS, "In World Smelting Controll for the lava");
//		conversions = cfg.get(CATEGORY_CONVERSIONS, "conversions", new String[] {"minecraft:cobblestone@minecraft:stone.1.0","minecraft:dirt.0@minecraft:stone.1.1"},"If this list is defined it will be used alongside the furnace recipes");
		furnaceRecipes = cfg.get(CATEGORY_CONVERSIONS, "furnaceRecipes", true, "Enable/Disable using smelting recipes for Lava Dynamics");
		partialBlock = cfg.get(CATEGORY_CONVERSIONS, "partialBlock", false, "Enable/Disable consuming of partial blocks such as leaves, grass, crops, etc ...");
	}

}