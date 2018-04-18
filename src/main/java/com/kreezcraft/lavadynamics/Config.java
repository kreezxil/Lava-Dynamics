package com.kreezcraft.lavadynamics;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {

	public static Configuration cfg = LavaDynamics.config;

	public static String CATEGORY_GENERAL = "general", CATEGORY_DIMENSIONS = "dimensions", CATEGORY_CONVERSIONS = "conversions (mappings)";

	public static Property debugMode, dimOverworld, dimNether, dimEnd, conversions, burping, furnaceRecipes, partialBlock;

	public static Property dimensions; //a kludge for now

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