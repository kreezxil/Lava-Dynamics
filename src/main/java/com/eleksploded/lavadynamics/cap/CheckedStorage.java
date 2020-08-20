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

		CompoundNBT volcano = new CompoundNBT();
		volcano.putBoolean("is", instance.isVolcano());
		if(instance.isVolcano()) {
			volcano.putInt("top", instance.getTop());
			volcano.putInt("cooldown", instance.getCooldown());
		}
		n.put("LD_Volcano", volcano);

		return n;
	}

	@Override
	public void readNBT(Capability<IChecked> capability, IChecked instance, Direction side, INBT nbt) {
		CompoundNBT n = (CompoundNBT)nbt;

		if(n.contains("LD_Checked")) {
			if(n.getBoolean("LD_Checked")) instance.check();
		}

		if(n.contains("LD_Volcano")) {
			CompoundNBT volcano = n.getCompound("LD_Volcano");
			if(volcano.getBoolean("is")) {
				instance.setVolcano(volcano.getInt("top"));
				instance.setCooldown(volcano.getInt("cooldown"));
			}
		}
	}
}
