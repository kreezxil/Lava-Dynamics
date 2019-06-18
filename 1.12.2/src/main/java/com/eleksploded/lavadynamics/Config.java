package com.eleksploded.lavadynamics;

import java.util.ArrayList;
import java.util.List;

import com.eleksploded.lavadynamics.proxy.CommonProxy;

import net.minecraftforge.common.config.Configuration;

public class Config {
	//-----------Config: to be honest, not exactly sure how this code works, I just know it does-----------//
	
	//Categories
	private static final String Volcano = "Volcano Settings";
	private static final String Debug = "Debug Settings";
	private static final String OreGen = "Ore Gen Values";

	//My Values
    public static int volcanoChance;
    public static int volcanoYLevel;
    public static int craterSize;
    public static int distanceToGenerate;
    public static boolean worldGen;
    public static int volcanoCooldown;
    public static int volcanoHeightBase;
    public static int volcanoHeightDeviation;
    public static int calderaradius;
    public static int calderadeviation;
    public static String[] validDimensions = new String[1];
    
    public static boolean genVolcanoDebug;
	public static boolean protectChunks;
	
	private static List<String> ores1 = new ArrayList<String>();
	private static List<Integer> chance1 = new ArrayList<Integer>();
	
	public static int oreChance;
	public static String[] ores;
	public static int[] chance;
    
    //File Shtuff
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception e1) {
            System.out.println("Problem loading config file! "+ e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }
    
    //Where I get my values and set up my file
    private static void initGeneralConfig(Configuration cfg) {
    	oreDefaults();
    	
    	validDimensions[0] = "overworld";
    	
    	volcanoChance = cfg.getInt("volcanoChance", Volcano, 5, 0, 100, "Precent chance of volcano to spawn");
    	volcanoYLevel = cfg.getInt("volcanoYLevel", Volcano, 10, 3, 255, "Approximate Y level of underground volcano lake");
    	craterSize = cfg.getInt("craterSize", Volcano, 15, 1, 100, "Approximate size of the crater");
    	distanceToGenerate = cfg.getInt("distanceToGenerate", Volcano, 100, 20, 100000, "How close a player needs to be to generate a volcano");
    	worldGen = cfg.getBoolean("worldGen", Volcano, false, "Generate volcanoes at worldgen instead of after ");
    	volcanoCooldown = cfg.getInt("volcanoCooldown", Volcano, 25, 1, 10000, "Cooldown between trying to generate a volcano");
    	validDimensions = cfg.getStringList("validDimensions", Volcano, validDimensions, "Valid Dimensions for Volcano Generation");
    	
    	volcanoHeightBase = cfg.getInt("VolcanoHeightBase", Volcano, 10, 6, 50, "The base height of the volcano from the center");
    	volcanoHeightDeviation = cfg.getInt("volcanoHeightDeviation", Volcano, 3, 0, 24, "The Deviation from the base height");
    	
    	calderaradius = cfg.getInt("calderaradius", Volcano, 5, 3, 10, "The base radius of the caldera atop the volcano");
    	calderadeviation = cfg.getInt("calderadeviation", Volcano, 2, 0, 4, "The Deviation from the caldera radius");
    	
    	protectChunks = cfg.getBoolean("protectChunks", Volcano, true, "Protect Chunks that contain a tile Entity?");
    	
    	String[] tmp1 = new String[ores1.size()];
    	for(String i : ores1){
    		int j = ores1.indexOf(i);
    		tmp1[j] = i;
    	}
    	
    	oreChance = cfg.getInt("oreChance", OreGen, 50, 1, 1000, "Chance for a stone to be an ore in a volcano (out of 1000)");
    	
    	ores = cfg.getStringList("ores", OreGen, tmp1, "Ores to generate in volcanoes. Be sure this matches chances (in order and length)");
    	
    	int[] tmp = new int[chance1.size()];
    	for(int i : chance1){
    		int j = chance1.indexOf(i);
    		tmp[j] = i;
    	}
    	
    	chance = cfg.get(OreGen, "Chances for said ore to spawn, be sure this matches ores (in order and length)", tmp, "REE").getIntList();
    	
    	genVolcanoDebug = cfg.getBoolean("genVolcanoDebug", Debug, false, "Debug outputs from Volcano Generation");
    }

	private static void oreDefaults() {
		ores1.add("minecraft:coal_ore");
		chance1.add(15);
		ores1.add("minecraft:iron_ore");
		chance1.add(4);
		ores1.add("minecraft:gold_ore");
		chance1.add(3);
		ores1.add("minecraft:quartz_ore");
		chance1.add(2);
		ores1.add("minecraft:lapis_ore");
		chance1.add(2);
		ores1.add("minecraft:redstone_ore");
		chance1.add(3);
		ores1.add("minecraft:diamond_ore");
		chance1.add(1);
		ores1.add("minecraft:emerald_ore");
		chance1.add(1);
	}
}
