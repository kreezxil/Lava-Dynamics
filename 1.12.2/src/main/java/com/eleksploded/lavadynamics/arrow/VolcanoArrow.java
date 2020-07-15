package com.eleksploded.lavadynamics.arrow;

import com.eleksploded.lavadynamics.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class VolcanoArrow extends ItemArrow {
	
	public VolcanoArrow() {
		this.setRegistryName(Reference.MODID, "VolcanoArrow");
		this.setCreativeTab(CreativeTabs.MISC);
	}
	
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
        EntityVolcanoArrow arrow = new EntityVolcanoArrow(worldIn, shooter);
        return arrow;
    }
}
