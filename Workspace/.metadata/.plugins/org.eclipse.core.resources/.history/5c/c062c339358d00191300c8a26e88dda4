package com.eleksploded.lavadynamics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eleksploded.lavadynamics.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.MODID, version = Reference.Version)
public class LavaDynamics {

	//Init Proxies
	@SidedProxy(modId=Reference.MODID,clientSide=Reference.cproxy, serverSide=Reference.sproxy)
	public static CommonProxy proxy;
	
	public static final Logger Logger = LogManager.getLogger("LavaDynamics");
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
    	
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    }
    
    @Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new VolcanoCommand());
	}
}
