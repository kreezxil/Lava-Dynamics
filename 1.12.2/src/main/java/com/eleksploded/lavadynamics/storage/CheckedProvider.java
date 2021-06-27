package com.eleksploded.lavadynamics.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CheckedProvider implements ICapabilitySerializable<NBTTagCompound> {

	CheckedHandler instance = new CheckedHandler();
	
	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing side) {
		if(cap == CheckedCap.checkedCap) {
			return (T) instance;
		} else {
			return null;
		}
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) CheckedCap.checkedCap.writeNBT(instance, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		CheckedCap.checkedCap.readNBT(instance, null, nbt);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return false;
	}
}
