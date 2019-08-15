package com.eleksploded.ldcompathelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = LDCompatHelper.MODID, name = LDCompatHelper.NAME, version = LDCompatHelper.VERSION)
@EventBusSubscriber(modid = LDCompatHelper.MODID)
public class LDCompatHelper
{
	public static final String MODID = "ldcompat";
	public static final String NAME = "LD Compatibility Helper";
	public static final String VERSION = "1.0";
	public static Logger logger = LogManager.getLogger(MODID);
	
	@SidedProxy(clientSide = "com.eleksploded.ldcompathelper.ClientProxy", serverSide = "com.eleksploded.ldcompathelper.CommonProxy")
	private static CommonProxy proxy;

	public static KeyBinding key = new KeyBinding("key.crash.desc",KeyConflictContext.UNIVERSAL, KeyModifier.ALT, Keyboard.KEY_V, "key.crash.category");

	static List<String> toWrite = new ArrayList<String>();
	Thread checkThread;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void Start(WorldEvent.Load e){
		if(e.getWorld().provider.getDimension() == 0){
			ErrorCheckerThread check = new ErrorCheckerThread(e.getWorld());
			checkThread = new Thread(check);
			checkThread.start();
		}
	}

	@EventHandler
	public void Stop(WorldEvent.Unload e){
		if(checkThread.isAlive()){
			checkThread.stop();
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.register();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		for(Entry<ItemStack, ItemStack> set : FurnaceRecipes.instance().getSmeltingList().entrySet()){

			String input = set.getKey().getCount() + " " + set.getKey().getUnlocalizedName() + "|" + set.getKey().getMetadata();
			String output = set.getValue().getCount() + " " + set.getValue().getUnlocalizedName() + "|" + set.getValue().getMetadata();

			StringBuilder builder = new StringBuilder();
			builder.append("Input: " + input);
			builder.append(", Output: " + output);

			toWrite.add(builder.toString());
		}
		write("LDCompatOut");
	}

	private void write(String fileName) {
		try {
			File file = new File(Minecraft.getMinecraft().mcDataDir, "logs/" + fileName + ".txt");
			if(file.exists()) Files.delete(file.toPath());

			file.createNewFile();
			Files.write(file.toPath(), toWrite);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public static void onEvent(KeyInputEvent event)
	{
		if (key.isPressed()) 
		{
			throw new RuntimeException("Forced Crash");
		}
	}
}
