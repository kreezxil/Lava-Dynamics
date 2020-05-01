package com.eleksploded.lavadynamics.proxy;

import com.eleksploded.lavadynamics.arrow.EntityVolcanoArrow;
import com.eleksploded.lavadynamics.arrow.RenderVolcanoArrow;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
    	RenderingRegistry.registerEntityRenderingHandler(EntityVolcanoArrow.class, RenderVolcanoArrow.VolcanoArrowRenderFactory.INSTANCE);
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
     
    public void init(FMLInitializationEvent e) {
    	
    }
    
    public void registerItemModels(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
}
