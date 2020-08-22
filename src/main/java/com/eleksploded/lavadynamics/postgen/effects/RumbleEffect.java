package com.eleksploded.lavadynamics.postgen.effects;

import com.eleksploded.lavadynamics.LavaDynamics;
import com.eleksploded.lavadynamics.postgen.PostGenEffect;
import com.eleksploded.lavadynamics.postgen.PostGenEffectUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RumbleEffect extends PostGenEffect {
	
	public RumbleEffect() {
		this.setRegistryName(new ResourceLocation(LavaDynamics.ModId, "rumble"));
	}
	
	public SoundEvent event = new SoundEvent(new ResourceLocation(LavaDynamics.ModId, "postgeneffect.rumble"));;
	
	@Override
	public void execute(Chunk chunk, int top) {
		World world = chunk.getWorld();
		BlockPos pos = PostGenEffectUtils.getVolcanoTop(chunk, top);
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), event, SoundCategory.AMBIENT, 3, 1);
	}
}
