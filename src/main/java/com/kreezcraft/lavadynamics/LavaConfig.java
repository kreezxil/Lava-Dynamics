package com.kreezcraft.lavadynamics;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Config(modid = LavaDynamics.MODID, category="")
public class LavaConfig {

//	public static Configuration cfg = LavaDynamics.config;
	
	@Config.Comment({
		"If any config setting is not preceded by comments #'s then you may delete that particular setting as it is no longer being used."
	})
	public static Notes notes = new Notes();
	
	@Config.Comment({
		"Overall mod behavior configuration that doesn't fit in a particular category yet."
	})
	public static General general = new General();
	
	@Config.Comment({
		"Attributes related to protecting things."
	})
	public static Protection protection = new Protection();
	
	@Config.Comment({
		"Controls how volatile and evil your volcanos are!"
	})
	@Config.Name("Volcano Settings")
	public static VolcanoSettings volcanoSettings = new VolcanoSettings();
	
	@Config.Comment({
		"Settings to help determine when walls and nodules get built"
	})
	public static Noise noise = new Noise();
	
	@Config.Comment({
		"Volcanoes have explosive qualities, these settings are for that feature"
	})
	public static Explosions explosions = new Explosions();
	
	@Config.Comment({
		"The lower and higher extra amount to randomly add to a vent to set the size of a plume"
	})
	public static Plumes plumes = new Plumes();
	
	@Config.Comment({
		"True or False to control if LavaDynamics runs in said dimension.",
		"In the case of dimsToAllow, simply add a dimension ID to allow the mod in it."
	})
	public static Dimensions dimensions = new Dimensions();
	
	@Config.Comment({
		"In World Smelting Control for the lava"
	})
	@Config.Name("Conversions (mappings)")
	public static Mappings mappings = new Mappings();
	@Config.Comment({
		"The higher the value, the rarer all ores generating in the volcanic wall are. default: 500"
	})
	@Config.Name("Ore Gen")
	public static OreGen oreGen = new OreGen();
	
	public static OnlyTheLava onlyTheLava = new OnlyTheLava();
	@Config.Comment({
		"Determine the size of the shaft, these settings effectively reduce the frequency of the volcanos, but make the experience more rewarding"
	})
	@Config.Name("Shaft Settings")
	public static ShaftSettings shaftSettings = new ShaftSettings();
	
	/*public static String CDIMENSIONS = "Dimensions";
	public static Property dimOverworld, dimNether, dimEnd, dimsToAllow;

	public static String CGENERAL = "General";
	public static Property debugMode, ignoreTheseMods;

	public static String CMAPS = "Conversions (mappings)";
	public static Property furnaceRecipes, partialBlock, smeltingBlacklist;

	public static String CVOLCANO = "Volcano Settings";
	public static Property volcanoChance, volcanoGen, maxYlevel, nodulePartChance, psuedoSurface;

	public static String CNOISE = "Noise";
	public static Property lowNoise, highNoise;

	public static String CEXPLOSION = "Explosions";
	public static Property maxExplosion, minExplosion, chanceExplosion;

	public static String CLAVA = "OnlyTheLava";
	public static Property lavaSpread, sourceBlock;

	public static String CPROTECT = "Protection";
	public static Property protection, preserveVillages, findVillageRange;

	public static String CPLUME = "Plumes";
	public static Property extraHt, minHt;

	public static String COREGEN = "Ore Gen";
	public static Property maxChance;

	public static String CSHAFT = "Shaft Settings";
	public static Property shaftSize;*/
	
	
	
	public static class Notes {
		
	}
	
	public static class General {
		@Config.Comment({
			"If true it will print messages to the player based on what you are doing in the protected zone, useful for helping Kreezxil debug the mod"
		})
		public boolean debugMode = false;
		
		@Config.Comment({
			"A comma separated list of mod domains (modids) to ignore. Example: \"minecraft,biomesoplenty\"",
			"Why? Because these mods especially Biomes O'Plenty don't play well with Lava Dynamics."
		})
		public String ignoreTheseMods = "biomesoplenty";
	}
	
	public static class Protection {
		@Config.Comment({
			"if true any tile entity will prevent a volcano from erupting in a chunk. Signs are tile entities btw. Setting this to false enables hardcore volcanoes. Meaning a play must neutralize lava pools below them to be truly safe."
		})
		public boolean protection = true;
		
		@Config.Comment({
			"Disable to allow Villages to be destroyed with volcanos"
		})
		public boolean preserveVillages = true;
		
		@Config.Comment({
			"The range from the lava event to scan for a village.",
			"Default: 10"
		})
		public int findVillageRange = 10;
	}
	
	public static class VolcanoSettings {
		@Config.Comment({
			"Percent chance a volcano or magma vent will occur.",
			"Default: 20"
		})
		public int volcanoChance = 20;
		
		@Config.Comment({
			"You can play with this value, but it's not for you.",
			"Default: seriously it will ignore you"
		})
		public boolean volcanoGen = false;
		
		@Config.Comment({
			"The y level for the approximate level of your surface.",
			"Default: 69"
		})
		public int psuedoSurface = 69;
		
		@Config.Comment({
			"The max y value to consider for turning lava into a magma vent.",
			"Default: 10"
		})
		public int maxYlevel = 10;
		
		@Config.Comment({
			"0 to 100 chance that part of the nodule is formed to protect the ore being generated"
		})
		public int nodulePartChance = 10;
	}
	
	public static class Noise {
		@Config.Comment({
			"The Minimum value for determining how many blocks above the current block is also lava before generating a wall component.",
			"Default: 5"
		})
		public int lowNoise = 5;
		
		@Config.Comment({
			"The Maximum value for determining how many blocks above the current block is also lava before generating a wall component.\nSetting this lower than 2 can cause lava to instantly turn to stone!",
			"Default: 2"
		})
		public int highNoise = 2;
	}
	
	public static class Explosions {
		@Config.Comment({
			"Double value for determining strength of random explosions.",
			"Note: Values higher than 10 can cause lag!",
			"Default: 10.0"
		})
		public double maxExplosion = 10.0;
		
		@Config.Comment({
			"The minimum value for determining strenth of random explosions.",
			"Default: 1.0"
		})
		public double minExplosion = 1.0;
		
		@Config.Comment({
			"Integer value from 0 to 100 for determining the chance that lava causes an explosion.",
			"Default: 5"
		})
		public int chanceExplosion = 5;
	}
	
	public static class Plumes {
		@Config.Comment({
			"Extra amount of source blocks to add to the magma vent.",
			"Default: 7"
		})
		public int extraHt = 7;
		
		@Config.Comment({
			"The minimum amount of source blocks to add to the magma vent.",
			"Default: 3"
		})
		public int minHt = 3;
	}
	
	public static class Dimensions {
		@Config.Comment({
			"It's honestly meant to run here"
		})
		@Config.Name("OverWorld")
		public boolean dimOverworld = true;
		
		@Config.Comment({
			"All that lava, probably you should allow it!"
		})
		@Config.Name("Nether")
		public boolean dimNether = false;
		
		@Config.Comment({
			"I don't see why it can't run here"
		})
		@Config.Name("The End")
		public boolean dimEnd = true;
		
		@Config.Comment({
			"Other dimensions to allow lava dynamics to run in.",
			"Default: empty list"
		})
		public int[] dimsToAllow = new int[0];
	}
	
	public static class Mappings {
		@Config.Comment({
			"Enable/Disable using smelting recipes for Lava Dynamics"
		})
		public boolean furnaceRecipes = true;
		
		@Config.Comment({
			"Enable/Disable consuming of partial blocks such as leaves, grass, crops, etc ..."
		})
		public boolean partialBlock = false;
		
		@Config.Comment({
			"A list of block ids not to smelt."
		})
		public String[] smeltingBlacklist = new String[0];
	}
	
	public static class OreGen {
		@Config.Comment({
			"A standard would be 100, but that make ore too common, so 500 makes it all 5x rarer"
		})
		public int maxChance = 500;
		
		
	}
	
	public static class OnlyTheLava {
		@Config.Comment({
			"The integer value from 0 to 100 for determining the chance that lava will spread.",
			"Default: 10"
		})
		public int lavaSpread = 10;
		
		@Config.Comment({
			"If enabled source blocks are generated when lava encounters a consumable.",
			"Otherwise if false, no new source lava block is generated.",
			"It's impossible to place a flowing lava, Minecraft turns it into a source block!",
			"Default: true"
		})
		public boolean sourceBlock = true;
	}
	
	public static class ShaftSettings {
		@Config.Comment({
			"3 sizes available. small, medium, and large.",
			"small is single block shaft.",
			"medium is 5 block shaft.",
			"large is 9 block shaft.",
			"Random is anyone of the 3 above.",
			"Note: If all the positions in the pattern for the size as centered on the polled position ",
			"is not lava source blocks then a volcano will not result.",
			"Default: large"
		})
		public String shaftSize = "large";
	}
	
	// Call this from CommonProxy.preInit(). It will create our config if it doesn't
	// exist yet and read the values if it does exist.
	/*public static void readConfig() {
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
		cfg.addCustomCategoryComment("NOTES",
				"If any config setting is not preceded by comments #'s then you may delete that particular setting as it is no longer being used.");

		cfg.addCustomCategoryComment(CGENERAL, "Overall mod behavior configuration that doesn't fit in a particular category yet.");
		debugMode = cfg.get(CGENERAL, "debugMode", false,
				"If true it will print messages to the player based on what you are doing in the protected zone, useful for helping Kreezxil debug the mod");
		ignoreTheseMods = cfg.get(CGENERAL, "ignoreTheseMods", "biomesoplenty",
				"A comma separated list of mod domains (modids) to ignore. Example: \"minecraft,biomesoplenty\"\nWhy? Because these mods especially Biomes O'Plenty don't play well with Lava Dynamics.");

		cfg.addCustomCategoryComment(CPROTECT, "Attributes related to protecting things.");
		protection = cfg.get(CPROTECT, "protection", true,
				"if true any tile entity will prevent a volcano from erupting in a chunk. Signs are tile entities btw. Setting this to false enables hardcore volcanoes. Meaning a play must neutralize lava pools below them to be truly safe.");
		preserveVillages = cfg.get(CPROTECT, "preserveVillages", true,
				"Disable to allow Villages to be destroyed with volcanos");
		findVillageRange = cfg.get(CPROTECT, "findVillageRange", 10,
				"The range from the lava event to scan for a village.\nDefault: 10");

		cfg.addCustomCategoryComment(CVOLCANO, "Controls how volatile and evil your volcanos are!");
		volcanoChance = cfg.get(CVOLCANO, "volcanoChance", 20,
				"Percent chance a volcano or magma vent will occur.\nDefault: 20");
		volcanoGen = cfg.get(CVOLCANO, "volcanoGen", false,
				"You can play with this value, but it's not for you.\nDefault: seriously it will ignore you");
		psuedoSurface = cfg.get(CVOLCANO, "psuedoSurface", 69,
				"The y level for the approximate level of your surface.\nDefault: 69");
		maxYlevel = cfg.get(CVOLCANO, "maxYlevel", 10,
				"The max y value to consider for turning lava into a magma vent.\nDefault: 10");
		nodulePartChance = cfg.get(CVOLCANO, "nodulePartChance", 10,
				"0 to 100 chance that part of the nodule is formed to protect the ore being generated");

		cfg.addCustomCategoryComment(CNOISE, "Settings to help determine when walls and nodules get built");
		lowNoise = cfg.get(CNOISE, "lowNoise", 5,
				"The Minimum value for determining how many blocks above the current block is also lava before generating a wall component.\nDefault: 5");
		highNoise = cfg.get(CNOISE, "highNoise", 2,
				"The Maximum value for determining how many blocks above the current block is also lava before generating a wall component.\nSetting this lower than 2 can cause lava to instantly turn to stone!\nDefault: 2");

		cfg.addCustomCategoryComment(CEXPLOSION,
				"Volcanoes have explosive qualities, these settings are for that feature");
		maxExplosion = cfg.get(CEXPLOSION, "maxExplosion", 10.0,
				"float value for determining strength of random explosions.\nNote: Values higher than 10 can cause lag!\nDefault: 10.0");
		minExplosion = cfg.get(CEXPLOSION, "minExplosion", 1.0,
				"The minimum value for determining strenth of random explosions.\nDefault: 1.0");
		chanceExplosion = cfg.get(CEXPLOSION, "chanceExplosion", 5,
				"Integer value from 0 to 100 for determining the chance that lava causes an explosion.\nDefault: 5");

		cfg.addCustomCategoryComment(CPLUME,
				"The lower and higher extra amount to randomly add to a vent to set the size of a plume");
		extraHt = cfg.get(CPLUME, "extraHt", 7, "Extra amount of source blocks to add to the magma vent.\nDefault: 7");
		minHt = cfg.get(CPLUME, "minHt", 3,
				"The minimum amount of source blocks to add to the magma vent.\nDefault: 3");

		cfg.addCustomCategoryComment(CDIMENSIONS,
				"True or False to control if LavaDynamics runs in said dimension.\nIn the case of dimsToAllow, simply add a dimension ID to allow the mod in it.");
		dimOverworld = cfg.get(CDIMENSIONS, "OverWorld", true, "It's honestly meant to run here");
		dimNether = cfg.get(CDIMENSIONS, "Nether", false, "All that lava, probably you should allow it!");
		dimEnd = cfg.get(CDIMENSIONS, "The End", true, "I don't see why it can't run here");
		dimsToAllow = cfg.get(CDIMENSIONS, "dimsToAllow", new int[0],
				"Other dimensions to allow lava dynamics to run in.\nDefault: empty list");

		cfg.addCustomCategoryComment(CMAPS, "In World Smelting Controll for the lava");
		// conversions = cfg.get(CMAPS, "conversions", new String[]
		// {"minecraft:cobblestone@minecraft:stone.1.0","minecraft:dirt.0@minecraft:stone.1.1"},"If
		// this list is defined it will be used alongside the furnace recipes");
		furnaceRecipes = cfg.get(CMAPS, "furnaceRecipes", true,
				"Enable/Disable using smelting recipes for Lava Dynamics");
		partialBlock = cfg.get(CMAPS, "partialBlock", false,
				"Enable/Disable consuming of partial blocks such as leaves, grass, crops, etc ...");
		smeltingBlacklist = cfg.get(CMAPS, "smeltingBlacklist", new String[0],
				"A list of block ids not to smelt.");

		cfg.addCustomCategoryComment(COREGEN,
				"The higher the value, the rarer all ores generating in the volcanic wall are. default: 500");
		maxChance = cfg.get(COREGEN, "maxChance", 500,
				"A standard would be 100, but that make ore too common, so 500 makes it all 5x rarer");

		lavaSpread = cfg.get(CLAVA, "lavaSpread", 10,
				"The integer value from 0 to 100 for determining the chance that lava will spread.\nDefault: 10");
		sourceBlock = cfg.get(CLAVA, "sourceBlock", true,
				"If enabled source blocks are generated when lava encounters a consumable.\nOtherwise if false, no new source lava block is generated. \nIt's impossible to place a flowing lava, Minecraft turns it into a source block!\nDefault: true");

		cfg.addCustomCategoryComment(CSHAFT,
				"Determine the size of the shaft, these settings effectively reduce the frequency of the volcanos, but make the experience more rewarding");
		shaftSize = cfg.get(CSHAFT, "shaftSize", "large",
				"3 sizes available. small, medium, and large.\nsmall is single block shaft.\nmedium is 5 block shaft.\nlarge is 9 block shaft.\nRandom is anyone of the 3 above.\nNote: If all the positions in the pattern for the size as centered on the polled position \nis not lava source blocks then a volcano will not result.\nDefault: large");
	}*/

}