package com.eleksploded.lavadynamics.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CheckedProvider implements ICapabilitySerializable<CompoundNBT> {

	CheckedHandler instance = new CheckedHandler();
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CheckedCap.checkedCap) {
			return LazyOptional.of(() -> instance).cast();
		} else {
			return LazyOptional.empty();
		}
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) CheckedCap.checkedCap.getStorage().writeNBT(CheckedCap.checkedCap, instance, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		CheckedCap.checkedCap.getStorage().readNBT(CheckedCap.checkedCap, instance, null, nbt);
	}

}
