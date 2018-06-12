package com.kreezcraft.lavadynamics;

import net.minecraftforge.common.config.Config;

@Config(modid = LavaDynamics.MODID, category="")
public class LavaConfig {
	
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
		@Config.RangeInt(min=0, max=100)
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
		@Config.RangeInt(min=0, max=100)
		public int nodulePartChance = 10;
	}
	
	public static class Noise {
		@Config.Comment({
			"The Minimum value for determining how many blocks above the current block is also lava before generating a wall component.",
			"Default: 5"
		})
		public int lowNoise = 5;
		
		@Config.Comment({
			"The Maximum value for determining how many blocks above the current block is also lava before generating a wall component.",
			"Setting this lower than 2 can cause lava to instantly turn to stone!",
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
		@Config.RangeInt(min=0, max=100)
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
		
//		@Config.Comment({
//			"If this list is defined it will be used alongside the furnace recipes"
//		})
//		public String[] conversions = new String[] {"minecraft:cobblestone@minecraft:stone.1.0","minecraft:dirt.0@minecraft:stone.1.1"};
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
		@Config.RangeInt(min=0, max=100)
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

}