package com.eleksploded.betterbeds.proxy;

import java.io.File;

import com.eleksploded.betterbeds.Config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "betterbeds.cfg"));
        Config.readConfig();
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
