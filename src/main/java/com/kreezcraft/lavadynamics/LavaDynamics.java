package com.kreezcraft.lavadynamics;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;
import java.util.Random;

import org.apache.logging.log4j.Logger;

import com.kreezcraft.lavadynamics.commands.CommandDebug;
import com.kreezcraft.lavadynamics.commands.CommandDimension;
import com.kreezcraft.lavadynamics.commands.CommandDisplay;
import com.kreezcraft.lavadynamics.commands.cmdLavaDynamics;
import com.kreezcraft.lavadynamics.commands.addCommands.CommandSrc;

@Mod(modid = LavaDynamics.MODID, name = LavaDynamics.NAME, version = LavaDynamics.VERSION)
public class LavaDynamics {
	/*
	 * Adding Vulcanism to Minecraft through Block Update Detection
	 * 
	 * Specifically when lava flows on a block, if the block has a smeltable block
	 * output recipe, the block will be dynamically updated to that output block.
	 */

	public static final String MODID = "lavadynamics";
	public static final String NAME = "Lava Dynamics";
	public static final String VERSION = "@VERSION@";

	public static Logger logger;
	public static Configuration config;
	public static LavaDynamics instance;
	public static Object source, destination;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		File directory = event.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "lavadynamics.cfg"));
		Config.readConfig();
		Config.volcanoGen.set(false);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void post(FMLPostInitializationEvent event) {
		if (config.hasChanged()) {
			config.save();
		}

	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		/*
		 * way too much time has been spent on commands and subcommands it is disabled
		 * for now
		 */
		event.registerServerCommand(new cmdLavaDynamics());
	}

}
