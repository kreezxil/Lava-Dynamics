package com.eleksploded.lavadynamics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eleksploded.lavadynamics.commands.VolcanoCommand;
import com.eleksploded.lavadynamics.proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
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

@Mod(modid = Reference.MODID, version = Reference.Version, updateJSON = "https://raw.githubusercontent.com/kreezxil/Lava-Dynamics/1.12.2-fixed/Updates.json")
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
		if(LavaConfig.general.genVolcanoDebug) {
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
	
	@SubscribeEvent
	void playerJoin(EntityJoinWorldEvent event){
		boolean update = ForgeVersion.getResult(Loader.instance().activeModContainer()).status == ForgeVersion.Status.UP_TO_DATE ? false : true;
		
		if(event.getEntity() instanceof EntityPlayerSP && update){
			EntityPlayerSP player = (EntityPlayerSP)event.getEntity();
			player.sendMessage(new TextComponentString("Update avaliable for LavaDynamimics"));
		}
	}
}
