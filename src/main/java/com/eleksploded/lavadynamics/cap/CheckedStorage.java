package com.eleksploded.lavadynamics.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CheckedStorage implements IStorage<IChecked> {

	@Override
	public INBT writeNBT(Capability<IChecked> capability, IChecked instance, Direction side) {
		
		CompoundNBT n = new CompoundNBT();
		
		n.putBoolean("LD_Checked", instance.isChecked());
		
		n.putBoolean("LD_Volcano", instance.isVolcano());
		if(instance.isVolcano()) n.putInt("LD_Top", instance.getTop());
		
		return n;
	}

	@Override
	public void readNBT(Capability<IChecked> capability, IChecked instance, Direction side, INBT nbt) {
		CompoundNBT n = (CompoundNBT)nbt;
		
		if(n.contains("LD_Checked")) {
			if(n.getBoolean("LD_Checked")) instance.check();
		}
		
		if(n.contains("LD_Volcano")) {
			if(n.getBoolean("LD_Volcano")) {
				instance.setVolcano(n.getInt("LD_Top"));
			}
		}
	}
}
