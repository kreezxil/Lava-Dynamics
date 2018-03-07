package com.kreezcraft.lavadynamics;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = LavaDynamics.MODID, name = LavaDynamics.NAME, version = LavaDynamics.VERSION)
public class LavaDynamics
{
   /*
    * Adding Vulcanism to Minecraft through Block Update Detection
    * 
    * Specifically when lava flows on a block, if the block has a smeltable block output recipe, 
    * the block will be dynamically updated to that output block.
    */
	
	public static final String MODID = "lavadynamics";
    public static final String NAME = "Lava Dynamics";
    public static final String VERSION = "@VERSION@";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some kreezcraft code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
