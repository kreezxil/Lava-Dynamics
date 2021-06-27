package com.eleksploded.lavadynamics.storage;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CheckedStorage implements IStorage<IChecked> {

	@Override
	public NBTBase writeNBT(Capability<IChecked> capability, IChecked instance, EnumFacing side) {

		NBTTagCompound n = new NBTTagCompound();

		n.setBoolean("LD_Checked", instance.isChecked());

		NBTTagCompound volcano = new NBTTagCompound();
		volcano.setBoolean("is", instance.isVolcano());
		if(instance.isVolcano()) {
			volcano.setInteger("top", instance.getTop());
			volcano.setInteger("cooldown", instance.getCooldown());
		}
		n.setTag("LD_Volcano", volcano);

		return n;
	}

	@Override
	public void readNBT(Capability<IChecked> capability, IChecked instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound n = (NBTTagCompound)nbt;

		if(n.getBoolean("LD_Checked")) instance.check();

		NBTTagCompound volcano = n.getCompoundTag("LD_Volcano");
		if(volcano.getBoolean("is")) {
			instance.setVolcano(volcano.getInteger("top"));
			instance.setCooldown(volcano.getInteger("cooldown"));
		}
	}
}
