package com.eleksploded.lavadynamics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eleksploded.lavadynamics.arrow.EntityVolcanoArrow;
import com.eleksploded.lavadynamics.arrow.VolcanoArrow;
import com.eleksploded.lavadynamics.commands.CheckedAddCommand;
import com.eleksploded.lavadynamics.commands.CheckedChunkCommand;
import com.eleksploded.lavadynamics.commands.ForcePostGenEffect;
import com.eleksploded.lavadynamics.commands.VolcanoCommand;
import com.eleksploded.lavadynamics.postgen.PostGenEffectRegistry;
import com.eleksploded.lavadynamics.postgen.RunEffects;
import com.eleksploded.lavadynamics.postgen.effects.RumbleEffect;
import com.eleksploded.lavadynamics.postgen.effects.erupt.DamageFallingBlock;
import com.eleksploded.lavadynamics.postgen.effects.erupt.EruptEffect;
import com.eleksploded.lavadynamics.proxy.CommonProxy;
import com.eleksploded.lavadynamics.threaded.GeneratorServerHandler;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = Reference.MODID, version = Reference.Version, updateJSON = "https://raw.githubusercontent.com/kreezxil/Lava-Dynamics/1.12.2-fixed/Updates.json")
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class LavaDynamics {

	//Init Volcano Block
	public static final Block VolcanoBlock = new VolcanoBlock();
	
	public static final VolcanoArrow volcanoarrow = new VolcanoArrow();

	//Init Proxies
	@SidedProxy(modId=Reference.MODID, clientSide=Reference.cproxy, serverSide=Reference.sproxy)
	public static CommonProxy proxy;

	public static final Logger Logger = LogManager.getLogger("LavaDynamics");

	@Instance
	public static LavaDynamics instance;

	public static boolean mineralogy = false;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) { 
		GameRegistry.registerWorldGenerator(new WorldGenVolcano(), 1);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(RunEffects.class);
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
	}

	RumbleEffect rumbleEffect = new RumbleEffect();
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		if(Loader.isModLoaded("mineralogy")) {
			//mineralogy = true;
		}
		
		PostGenEffectRegistry.registerEffect(new EruptEffect());
		PostGenEffectRegistry.registerEffect(rumbleEffect);
		
		if(LavaConfig.general.useThreadedGeneration) {
			MinecraftForge.EVENT_BUS.register(GeneratorServerHandler.class);
		} else {
			MinecraftForge.EVENT_BUS.register(Volcano.class);
		}
		
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CheckedAddCommand());
		event.registerServerCommand(new VolcanoCommand());
		event.registerServerCommand(new CheckedChunkCommand());
		event.registerServerCommand(new ForcePostGenEffect());
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
		registry.register(itemblock);
		Item item = Item.getItemFromBlock(VolcanoBlock);
		proxy.registerItemModels(item, 0, "Inventory");
		
		registry.register(volcanoarrow);
		proxy.registerItemModels(volcanoarrow, 0, "Inventory");
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	static void playerJoin(EntityJoinWorldEvent event){
		Status update = ForgeVersion.getResult(Loader.instance().activeModContainer()).status;
		try{
			if(event.getEntity() instanceof EntityPlayerSP && update == ForgeVersion.Status.OUTDATED){
				EntityPlayerSP player = (EntityPlayerSP)event.getEntity(); 
				player.sendMessage(new TextComponentString("An update avaliable for ").appendSibling(new TextComponentString("LavaDynamics").setStyle(new Style().setColor(TextFormatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/lava-dynamics/files/")))));
			}
		} catch(NoClassDefFoundError e){
			if(update == ForgeVersion.Status.OUTDATED) {
				Logger.info("New update avalable for LavaDynamics");
			}
		}
	}

	@SubscribeEvent
	public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {	
		event.getRegistry().register(rumbleEffect.event.setRegistryName(Reference.MODID, "rumble"));		
	}

	@SubscribeEvent
	public void registerEntity(RegistryEvent.Register<EntityEntry> event) {
		EntityEntry damageFallingBlock = EntityEntryBuilder.create().entity(DamageFallingBlock.class)
				.id(new ResourceLocation(Reference.MODID, "DamageFallingBlock"), 0)
				.name("DamageFallingBlock")
				.tracker(80, 3, true)
				.build();
		EntityEntry volcanoArrow = EntityEntryBuilder.create().entity(EntityVolcanoArrow.class)
				.id(new ResourceLocation(Reference.MODID,  "volcanoarrow"), 33)
				.name("VolcanoArrow")
				.tracker(80, 1, true)
				.build();
		
		IForgeRegistry<EntityEntry> reg = event.getRegistry();

		reg.register(damageFallingBlock);
		reg.register(volcanoArrow);

	}
}
