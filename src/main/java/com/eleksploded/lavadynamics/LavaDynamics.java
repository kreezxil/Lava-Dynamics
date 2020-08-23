package com.eleksploded.lavadynamics;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eleksploded.eleklib.config.Config;
import com.eleksploded.eleklib.config.ConfigBuilder;
import com.eleksploded.lavadynamics.cap.CheckedHandler;
import com.eleksploded.lavadynamics.cap.CheckedStorage;
import com.eleksploded.lavadynamics.cap.IChecked;
import com.eleksploded.lavadynamics.command.CheckedCommand;
import com.eleksploded.lavadynamics.command.SpawnVolcano;
import com.eleksploded.lavadynamics.postgen.PostGenEffect;
import com.eleksploded.lavadynamics.postgen.effects.EruptEffect;
import com.eleksploded.lavadynamics.postgen.effects.RumbleEffect;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(LavaDynamics.ModId)
public class LavaDynamics
{
	public static final String ModId = "lavadynamics";
	public static final Logger Logger = LogManager.getLogger();

	public static Config LavaConfig;

	public LavaDynamics() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onComplete);

		MinecraftForge.EVENT_BUS.register(this);

		LavaConfig = ConfigBuilder.builder()
				.category("General", "General Settings", b -> {
					b.addBool("debug", false, "Enable debug mode. Will result in console spam");
					b.addBool("postgendebug", false, "Enable debug mode for postgen. Will result in console spam");
					b.addBool("regDump", false, "Causes the mod to print all PostGenEffect registry contents to the console");
				})
				.category("Generator", "Generation Settings", b -> {
					b.category("Restrictions", "Chances/restrictions of where to spawn", c -> {
						c.addInt("chance", 5, "Chance for a volcano to spawn. x in 1000", 0, 1000);

						c.addBool("tile_protect", true, "Protect chunks containing tile entities");

						c.addBool("spawnChunks", false, "Should Volcanoes spawn in spawn chunks");
						c.addInt("spawnDistance", 500, "Block distance from spawn needed to spawn a volcano", 0, 10000);

						c.addInt("playerDistance", 100, "Distance from a player needed before spawning a volcano", 0, 10000);
						c.addInt("volcanoDistance", 16, "Distance from other volcanoes to spawn a volcano (in chunks, multiply by 16 for blocks). Scans a square of radius this.", 0, 128);
					});
					b.category("Biome", "Options for volcanoes using biomes", c -> {
						c.addBool("useBiome", true, "Should the volcano use the biome blocks");

						c.addInt("fillerSize", 2, "How far in should biome filler blocks go? Set to 0 to disable use of filler blocks", 0, 64);
						c.addInt("biomeStart", 3, "How far down should biome meshing start?", 0, 128);
					});
					b.addInt("volcanoYLevel", 10, "Approximate Y level of underground volcano lake", 0, 64);

					b.addInt("heightMin", 3, "Minimum Height of the volcano", 3, 100);
					b.addInt("heightMax", 20, "Maximum Height of the volcano", 4, 101);

					b.addInt("calderaMin", 2 , "Minimum size of the calderas", 2, 20);
					b.addInt("calderaMax", 6 , "Maximum size of the calderas", 2, 20);

					b.addInt("craterSize", 15,"Power of Initial Eruption", 0, 128);
					b.addBool("initialFire", true, "Should the initial eruption cause fire");

					b.addInt("oreChance", 50, "Chance an ore spawns. x in 1000", 0, 1000);
					b.addValue("ores", Arrays.asList(new String[] {"minecraft:coal_ore|15"}), "Ores to spawn in volcano. Format should be 'modid:block|chances' ");
				}).category("PostGenEffects", "Options for PostGen Effects", b -> {
					b.addInt("postGenEffectChance", 5, "Chance of an effect occuring (out of 1000). Set to 0 to disable.", 0, 1000);
					
					b.addInt("PostGenEffectCooldown", 1000, "Minumum ticks between effects", 0, 1728000);
					
					b.addValue("blacklistEffects", Arrays.asList(new String[] {}), "List of effects to blacklist, just put the effect name here. Ex: \"erupt\"");
				}).category("WorldSmelting", "Options to do with world smelting", b -> {
					b.addBool("worldSmeltingEnabled", true, "Is world smelting enabled");
					b.addValue("blacklistedBlocks", Arrays.asList(new String[] {}), "Block IDs to ignore smelting (modid:block)");
				}).category("Performance", "Options that hava impact on performance", b -> {
					b.addInt("cacheSize", 256, "Size of the cache of chunks. Larger Caches will speed up world gen time, at the cost of RAM usage. Set to 0 to disable caching.", 0, 65536);
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
		SpawnVolcano.register(e.getDispatcher());
	}
	
	public void onComplete(FMLLoadCompleteEvent e) {
		if(LavaConfig.getBool("regDump")) {
			System.out.println("Dumping PostGenEffect Registry...");
			IForgeRegistry<PostGenEffect> reg = GameRegistry.findRegistry(PostGenEffect.class);
			reg.forEach(effect -> {
				System.out.println("	" + effect.getRegistryName());
			});
			System.out.println("Dump Complete");
		}
	}
	
	public static class LDPostGenEffects {
		public static PostGenEffect erupt = new EruptEffect();
		public static PostGenEffect rumble = new RumbleEffect();
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void onRegRegistry(final RegistryEvent.NewRegistry reg) {
			new RegistryBuilder<PostGenEffect>()
			.setName(new ResourceLocation(ModId, "postgeneffects"))
			.setType(PostGenEffect.class)
			.setDefaultKey(new ResourceLocation(ModId, "invalid"))
			.create();
		}

		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
			
		}
		
		@SubscribeEvent
		public static void onPostGenRegistry(final RegistryEvent.Register<PostGenEffect> pge) {
			IForgeRegistry<PostGenEffect> reg = pge.getRegistry();
			
			reg.register(LDPostGenEffects.erupt);
			reg.register(LDPostGenEffects.rumble);
		}
	}
}
