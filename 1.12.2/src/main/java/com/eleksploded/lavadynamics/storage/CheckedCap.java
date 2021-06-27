package com.eleksploded.lavadynamics.storage;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "lavadynamics")
public class CheckedCap {
	
	public static final String ThrowError = "Error getting Checked Capability, Please report on the github page";
	public static ResourceLocation CheckedKey = new ResourceLocation("lavadynamics", "checkedcap");
	@CapabilityInject(value = IChecked.class)
	public static Capability<IChecked> checkedCap = null;
	
	@SubscribeEvent
	public static void attachCaps(AttachCapabilitiesEvent<Chunk> e) {
		e.addCapability(CheckedKey, new CheckedProvider());
	}
}
