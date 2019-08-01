package com.eleksploded.ldcompathelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = LDCompatHelper.MODID, name = LDCompatHelper.NAME, version = LDCompatHelper.VERSION)
public class LDCompatHelper
{
	public static final String MODID = "ldcompat";
	public static final String NAME = "LD Compatibility Helper";
	public static final String VERSION = "1.0";
	public static Logger logger = LogManager.getLogger(MODID);

	static List<String> toWrite = new ArrayList<String>();

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
		write();
	}

	private void write() {
		try {
			File file = new File(Minecraft.getMinecraft().mcDataDir, "logs/LDCompatOut.txt");
			if(file.exists()) Files.delete(file.toPath());
			
			file.createNewFile();
			Files.write(file.toPath(), toWrite);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
