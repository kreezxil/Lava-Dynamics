package com.eleksploded.lavadynamics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eleksploded.lavadynamics.proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = Reference.MODID, version = Reference.Version)
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class LavaDynamics {

	//Init Volcano Block
	public static final Block VolcanoBlock = new VolcanoBlock();

	//Init Proxies
	@SidedProxy(modId=Reference.MODID,clientSide=Reference.cproxy, serverSide=Reference.sproxy)
	public static CommonProxy proxy;

	public static final Logger Logger = LogManager.getLogger("LavaDynamics");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) { 
		GameRegistry.registerWorldGenerator(new WorldGenVolcano(), 1);
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event){
		//Dont really need this, but meh
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		//Register '/spawnvolcano' command
		event.registerServerCommand(new VolcanoCommand());
	}

	@SubscribeEvent
	public static void blocks(RegistryEvent.Register<Block> event) {
		if(true) {
			Logger.info("Registering Blocks");
		}
		//Register VolcanoBlck
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(VolcanoBlock);
	}

	@SubscribeEvent
	public static void items(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		//Register VolcanoBlock Item, cause why not
		ItemBlock itemblock = new ItemBlock(VolcanoBlock);
		itemblock.setRegistryName(VolcanoBlock.getRegistryName());
		itemblock.setUnlocalizedName(VolcanoBlock.getUnlocalizedName());
		registry.register(itemblock);
		Item item = Item.getItemFromBlock(VolcanoBlock);
		proxy.registerItemModels(item, 0, "Inventory");
	}
}
