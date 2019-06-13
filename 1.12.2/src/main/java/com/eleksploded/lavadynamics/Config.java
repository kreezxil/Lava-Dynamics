package com.eleksploded.lavadynamics;

import com.eleksploded.lavadynamics.proxy.CommonProxy;

import net.minecraftforge.common.config.Configuration;

public class Config {
	//-----------Config: to be honest, not exactly sure how this code works, I just know it does-----------//
	
	//Categories
	private static final String Volcano = "Volcano Settings";
	private static final String Debug = "Debug Settings";

	//My Values
    public static int volcanoChance;
    public static int volcanoYLevel;
    public static int craterSize;
    public static int distanceToGenerate;
    public static boolean worldGen;
    public static int volcanoCooldown;
    public static String[] validDimensions = new String[1];
    
    public static boolean genVolcanoDebug;
    
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
    	validDimensions[0] = "overworld";
    	volcanoChance = cfg.getInt("volcanoChance", Volcano, 5, 0, 100, "Precent chance of volcano to spawn");
    	volcanoYLevel = cfg.getInt("volcanoYLevel", Volcano, 10, 3, 255, "Approximate Y level of underground volcano lake");
    	craterSize = cfg.getInt("craterSize", Volcano, 15, 1, 100, "Approximate size of the crater");
    	distanceToGenerate = cfg.getInt("distanceToGenerate", Volcano, 100, 20, 100000, "How close a player needs to be to generate a volcano");
    	worldGen = cfg.getBoolean("worldGen", Volcano, false, "Generate volcanoes at worldgen instead of after ");
    	volcanoCooldown = cfg.getInt("volcanoCooldown", Volcano, 25, 1, 10000, "Cooldown between trying to generate a volcano");
    	validDimensions = cfg.getStringList("validDimensions", Volcano, validDimensions, "Valid Dimensions for Volcano Generation");
    	
    	genVolcanoDebug = cfg.getBoolean("genVolcanoDebug", Debug, false, "Debug outputs from Volcano Generation");
    }
}
