package com.eleksploded.lavadynamics;

import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MODID, category="")
public class LavaConfig {
	
	@Config.Name("General Config")
	public static General general = new General();
	
	@Config.Name("Volcano Settings")
	public static VolcanoSettings volcano = new VolcanoSettings();
	
	public static class General {
		@Config.Comment("Enable debug outputs from Volcano Generation")
		public boolean genVolcanoDebug = false;
	}

	public static class VolcanoSettings {
		@Config.Comment("Precent chance of volcano to spawn")
		@Config.RangeInt(min=0,max=100)
		public int volcanoChance = 5;
		
		@Config.Comment("Approximate Y level of underground volcano lake")
		@Config.RangeInt(min=3,max=255)
		public int volcanoYLevel = 10;
		
		@Config.Comment("Power of Initial Eruptuin")
		@Config.RangeInt(min=1,max=128)
		public int craterSize = 15;
		
		@Config.Comment("Generate volcanoes at worldgen instead of after")
		public boolean worldGen = false;
		
		@Config.Comment("Cooldown between trying to generate a volcano")
		@Config.RangeInt(min=1,max=100000)
		public int volcanoCooldown = 25;
		
		@Config.Comment("List of valid Dimension IDs to spawn volcanoes in")
		public int[] validDimensions = { 0 };

		@Config.Comment("Minimum Height of the volcano")
		@Config.RangeInt(min=3,max=100)
		public int volcanoHeightMin = 3;
		
		@Config.Comment("Maximum Height of the volcano")
		@Config.RangeInt(min=4,max=101)
		public int volcanoHeightMax = 20;
		
		@Config.Comment("Minimum size of the calderas")
		@Config.RangeInt(min=2,max=20)
		public int calderaMin = 2;
		
		@Config.Comment("Maximum size of the calderas")
		@Config.RangeInt(min=2,max=20)
		public int calderaMax = 6;
		
		@Config.Comment("Protect Chunks that contain a tile Entity?")
		public boolean protectChunks = true;
		
		@Config.Comment("Chance a underwater vent turns into a full volcano")
		@Config.RangeInt(min=0,max=100)
		public int waterVolcanoChance = 30;


		@Config.Comment("Chance for a stone to be an ore in a volcano (out of 1000), Default is 5%")
		@Config.RangeInt(min=0,max=1000)
		public int oreChance = 50;

		@Config.Comment("Ores to generate in volcanoes. Be sure this matches chances (in order and length)")
		public String[] ores = {"minecraft:coal_ore", "minecraft:iron_ore", "minecraft:gold_ore", "minecraft:quartz_ore", 
				"minecraft:lapis_ore", "minecraft:redstone_ore", "minecraft:diamond_ore", "minecraft:emerald_ore"};

		@Config.Comment("Amount of chances for said ore to spawn, be sure this matches ores (in order and length)")
		public int[] chance = {15,4,3,2,2,3,1,1};
	}

}


