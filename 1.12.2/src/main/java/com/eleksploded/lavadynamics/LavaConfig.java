package com.eleksploded.lavadynamics;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MODID, category="")
public class LavaConfig {
	
	@Config.Name("General Config")
	public static General general = new General();
	
	@Config.Name("Volcano Settings")
	public static VolcanoSettings volcano = new VolcanoSettings();
	
	@Config.Name("PostGen Effect Settings")
	public static PostGenEffectSettings postgen = new PostGenEffectSettings();
	
	@Config.Name("World Smelting Settings")
	public static WorldSmeltingOptions worldSmelt = new WorldSmeltingOptions();
	
	public static class General {
		@Config.Comment("Enable debug outputs from Volcano Generation")
		public boolean genVolcanoDebug = false;
		
		@Config.Comment("Enable debug outputs from Post generation volcano effects")
		public boolean postGenDebug = false;
	}
	
	public static class PostGenEffectSettings{
		@Config.Comment("List of effects to blacklist, just put the effect name here. Ex: \"erupt\"")
		public String[] effectBlacklist = {};
		
		@Config.Comment("Chance of an effect occuring (out of 1000). Set to 0 to disable.")
		@Config.RangeInt(min=0,max=1000)
		public int chance = 5;
		
		@Config.Comment("Time between effect checks (in minutes, given 20tps)")
		@Config.RangeDouble(min=.1D, max=60D)
		public double effectTime = 2D;
	}

	public static class VolcanoSettings {
		@Config.Comment("Precent chance of volcano to spawn")
		@Config.RangeInt(min=0,max=100)
		public int volcanoChance = 5;
		
		@Config.Comment("Allow spawning of volcanoes in spawn chunks")
		public boolean spawnChunks = false;
		
		@Config.Comment("Distance from spawn a volcano must obey")
		@Config.RangeInt(min=1, max = 100000)
		public int spawnDistance = 500;
		
		@Config.Comment("Approximate Y level of underground volcano lake")
		@Config.RangeInt(min=3,max=255)
		public int volcanoYLevel = 10;
		
		@Config.Comment("Minimum distance from player required before generating a volcano")
		@Config.RangeInt(min=0,max=5000)
		public int minimumDistance = 100;
		
		@Config.Comment("Power of Initial Eruption")
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
		
		@Config.Comment("Type of volcanoes to generate in specific biomes, Not active yet")
		public String[] noCone = {""};
		
		@Config.Comment("Chance a underwater vent turns into a full volcano")
		@Config.RangeInt(min=0,max=100)
		public int waterVolcanoChance = 30;

		@Config.Comment("Chance for a stone to be an ore in a volcano (out of 1000), Default is 5%")
		@Config.RangeInt(min=0,max=1000)
		public int oreChance = 50;

		@Config.Comment("Ores to generate in volcanoes. Be sure this matches chances (in order and length). Syntax is \"modId:blockname|metadata\"")
		public String[] ores = {"minecraft:coal_ore|0", "minecraft:iron_ore|0", "minecraft:gold_ore|0", "minecraft:quartz_ore|0", 
				"minecraft:lapis_ore|0", "minecraft:redstone_ore|0", "minecraft:diamond_ore|0", "minecraft:emerald_ore|0"};

		@Config.Comment("Amount of chances for said ore to spawn, be sure this matches ores (in order and length)")
		public int[] chance = {15,4,3,2,2,3,1,1};
		
		@Config.Comment("Allow volcanoes to spawn in already checked chunks. Also ignores distance entry. May cause volcanoes to spawn inside eachother")
		public boolean disaster = false;
		
		@Config.Comment("Distance from other volcanoes needed to spawn a new volcano")
		@Config.RangeInt(min=0,max=100000)
		public int distance = 500;
	}
	
	public static class WorldSmeltingOptions {
		
		@Config.Comment({"Blocks to blacklist inworld smelting", 
			"Add the result here. For example, to stop sand from smelting into glass, you would add 'minecraft:glass' to the list"})
		public String[] blacklist = {};
	}
	
	@Mod.EventBusSubscriber(modid = Reference.MODID)
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MODID)) {
				ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
			}
		}
	}
}


