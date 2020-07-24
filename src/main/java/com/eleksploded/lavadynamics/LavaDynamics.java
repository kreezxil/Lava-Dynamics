package com.eleksploded.lavadynamics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eleksploded.eleklib.config.Config;
import com.eleksploded.eleklib.config.ConfigBuilder;
import com.eleksploded.lavadynamics.cap.CheckedHandler;
import com.eleksploded.lavadynamics.cap.CheckedStorage;
import com.eleksploded.lavadynamics.cap.IChecked;
import com.eleksploded.lavadynamics.command.CheckedCommand;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lavadynamics")
public class LavaDynamics
{
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static Config LavaConfig;
    
    public LavaDynamics() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        
        LavaConfig = ConfigBuilder.builder()
        	.category("Generator", "Generation Settings", b -> {
        		b.addInt("chance", 5, "Chance for a volcano to spawn", 0, 100);
        		
        		b.addBool("spawnChunks", false, "Should Volcanoes spawn in spawn chunks");
        		b.addInt("spawnDistance", 500, "Block distance from spawn needed to spawn a volcano", 0, 10000);
        	
        		b.addInt("playerDistance", 100, "Distance from a player needed before spawning a volcano", 0, 10000);
        		b.addInt("volcanoDistance", 500, "Distance from other volcanoes to spawn a volcano", 0, 10000);
        	})
        .build("lavadynamics", ModConfig.Type.COMMON);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	CapabilityManager.INSTANCE.register(IChecked.class, new CheckedStorage(), CheckedHandler::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	
    }
    
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    	
    }
    
    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent e) {
    	CheckedCommand.register(e.getDispatcher());
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        	
        }
    }
}
