package com.eleksploded.lavadynamics.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy {
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }
    
    public void registerItemModels(Item item, int meta, String id) {
    }
}
