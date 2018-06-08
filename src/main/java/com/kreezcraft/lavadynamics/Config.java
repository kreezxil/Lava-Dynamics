package com.kreezcraft.lavadynamics;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {

	public static Configuration cfg = LavaDynamics.config;

	public static String CATEGORY_GENERAL = "general", CATEGORY_DIMENSIONS = "dimensions",
			CATEGORY_CONVERSIONS = "conversions (mappings)", CVOLCANO = "Volcano Settings",
			CPROTECTION = "protection", CSHAFT = "Shaft Settings";
	
	public static String CNOISE = "noise", CEXPLOSIONS = "explosions", CPLUME = "plume height", CAT_OREGEN = "oreGen",
			CLAVA = "justTheLava";

	public static Property debugMode, dimOverworld, dimNether, dimEnd, conversions, burping, furnaceRecipes,
			partialBlock, volcanoChance, preserveVillages, findVillageRange;

	public static Property dimensions; // a kludge for now

	public static Property volcanoGen, lowNoise, highNoise, maxExplosion, minExplosion, chanceExplosion, lavaSpread,
			psuedoSurface, extraHt, minHt, maxYlevel;
	public static Property nodulePartChance, protection, maxChance, sourceBlock;
	public static Property ignoreTheseMods;
	
	public static Property shaftSize;

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
		cfg.addCustomCategoryComment("NOTES", "If any config setting is not preceded by comments #'s then you may delete that particular setting as it is no longer being used.");
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Settings");
		cfg.addCustomCategoryComment(CPROTECTION, "Protection Settings");
		debugMode = cfg.get(CATEGORY_GENERAL, "debugMode", false,
				"If true it will print messages to the player based on what you are doing in the protected zone, useful for helping Kreezxil debug the mod");

		volcanoChance = cfg.get(CVOLCANO, "volcanoChance", 20,
				"Percent chance a volcano or magma vent will occur.\nDefault: 20");
		volcanoGen = cfg.get(CVOLCANO, "volcanoGen", false,
				"You can play with this value, but it's not for you.\nDefault: seriously it will ignore you");
		protection = cfg.get(CPROTECTION, "protection", true,
				"if true any tile entity will prevent a volcano from erupting in a chunk. Signs are tile entities btw. Setting this to false enables hardcore volcanoes. Meaning a play must neutralize lava pools below them to be truly safe.");
		preserveVillages = cfg.get(CPROTECTION, "preserveVillages", true, "Disable to allow Villages to be destroyed with volcanos");
		findVillageRange = cfg.get(CPROTECTION, "findVillageRange", 10, "The range from the lava event to scan for a village.\nDefault: 10");
		psuedoSurface = cfg.get(CVOLCANO, "psuedoSurface", 69,
				"The y level for the approximate level of your surface.\nDefault: 69");

		cfg.addCustomCategoryComment(CNOISE, "Settings to help determine when walls and nodules get built");
		lowNoise = cfg.get(CNOISE, "lowNoise", 0,
				"The Minimum value for determining how many blocks above the current block is also lava before generating a wall component.\nDefault: 0");
		highNoise = cfg.get(CNOISE, "highNoise", 2,
				"The Maximum value for determining how many blocks above the current block is also lava before generating a wall component.\nSetting this lower than 2 can cause lava to instantly turn to stone!\nDefault: 2");

		cfg.addCustomCategoryComment(CEXPLOSIONS,
				"Volcanoes have explosive quailities, these settings are for that feature");
		maxExplosion = cfg.get(CEXPLOSIONS, "maxExplosion", 10.0,
				"float value for determining strength of random explosions.\nNote: Values higher than 10 can cause lag!\nDefault: 10.0");
		minExplosion = cfg.get(CEXPLOSIONS, "minExplosion", 1.0,
				"The minimum value for determining strenth of random explosions.\nDefault: 1.0");
		chanceExplosion = cfg.get(CEXPLOSIONS, "chanceExplosion", 5,
				"Integer value from 0 to 100 for determining the chance that lava causes an explosion.\nDefault: 5");

		cfg.addCustomCategoryComment(CPLUME,
				"the lower and higher extra amount to randomly add to a vent to set the size of a plume");
		extraHt = cfg.get(CPLUME, "extraHt", 10,
				"Extra amount of source blocks to add to the magma vent.\nDefault: 10");
		minHt = cfg.get(CPLUME, "minHt", 3,
				"The minimum amount of source blocks to add to the magma vent.\nDefault: 3");

		maxYlevel = cfg.get(CVOLCANO, "maxYlevel", 10,
				"The max y value to consider for turning lava into a magma vent.\nDefault: 10");
		nodulePartChance = cfg.get(CVOLCANO, "nodulePartChance", 10,
				"0 to 100 chance that part of the nodule is formed to protect the ore being generated");
		ignoreTheseMods = cfg.get(CLAVA, "ignoreTheseMods", "biomesoplenty","A comma separated list of mod domains (modids) to ignore. Example: \"minecraft,biomesoplenty\"\nWhy? Because these mods especially Biomes O'Plenty don't play well with Lava Dynamics.");
		
		// cfg.addCustomCategoryComment(CATEGORY_DIMENSIONS, "Dimensions not listed are
		// not allowed to function with Lava Dynamics. For instance don't
		// list\n"+"Nether and you won't have a million blocks tyring to update their
		// neighbors!");
		// dimensions = cfg.get(CATEGORY_DIMENSIONS, "dimensions", new int[]{0}, "List
		// of dimension id's for which the mod is allowed to operate");

		cfg.addCustomCategoryComment(CATEGORY_DIMENSIONS,
				"True or False to control if LavaDynamics runs in said dimension");
		dimOverworld = cfg.get(CATEGORY_DIMENSIONS, "OverWorld", true, "It's honestly meant to run here");
		dimNether = cfg.get(CATEGORY_DIMENSIONS, "Nether", false, "All that lava, probably you should allow it!");
		dimEnd = cfg.get(CATEGORY_DIMENSIONS, "The End", true, "I don't see why it can't run here");

		cfg.addCustomCategoryComment(CATEGORY_CONVERSIONS, "In World Smelting Controll for the lava");
		// conversions = cfg.get(CATEGORY_CONVERSIONS, "conversions", new String[]
		// {"minecraft:cobblestone@minecraft:stone.1.0","minecraft:dirt.0@minecraft:stone.1.1"},"If
		// this list is defined it will be used alongside the furnace recipes");
		furnaceRecipes = cfg.get(CATEGORY_CONVERSIONS, "furnaceRecipes", true,
				"Enable/Disable using smelting recipes for Lava Dynamics");
		partialBlock = cfg.get(CATEGORY_CONVERSIONS, "partialBlock", false,
				"Enable/Disable consuming of partial blocks such as leaves, grass, crops, etc ...");

		cfg.addCustomCategoryComment(CAT_OREGEN,
				"The higher the value, the rarer all ores generating in the volcanic wall are. default: 500");
		maxChance = cfg.get(CAT_OREGEN, "maxChance", 500,
				"A standard would be 100, but that make ore too common, so 500 makes it all 5x rarer");

		lavaSpread = cfg.get(CLAVA, "lavaSpread", 10,
				"The integer value from 0 to 100 for determining the chance that lava will spread.\nDefault: 10");
		sourceBlock = cfg.get(CLAVA, "sourceBlock", true, "If enabled source blocks are generated when lava encounters a consumable.\nOtherwise if false, no new source lava block is generated. \nIt's impossible to place a flowing lava, Minecraft turns it into a source block!\nDefault: true");

		cfg.addCustomCategoryComment(CSHAFT, "Determine the size of the shaft, these settings effectively reduce the frequency of the volcanos, but make the experience more rewarding");
		shaftSize = cfg.get(CSHAFT, "shaftSize", "large", "3 sizes available. small, medium, and large.\nsmall is single block shaft.\nmedium is 5 block shaft.\nlarge is 9 block shaft.\nRandom is anyone of the 3 above.\nNote: If all the positions in the pattern for the size as centered on the polled position \nis not lava source blocks then a volcano will not result.\nDefault: large");
	}

}