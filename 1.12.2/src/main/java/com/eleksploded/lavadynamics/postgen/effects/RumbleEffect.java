package com.eleksploded.lavadynamics.postgen.effects;

import com.eleksploded.lavadynamics.Reference;
import com.eleksploded.lavadynamics.postgen.IPostGenEffect;
import com.eleksploded.lavadynamics.postgen.PostGenEffectUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RumbleEffect implements IPostGenEffect {
	
	@Override
	public String getName() {
		return "rumble";
	}
	
	public SoundEvent event = new SoundEvent(new ResourceLocation(Reference.MODID, "postgeneffect.rumble"));;
	
	@Override
	public void execute(Chunk chunk, int top) {
		World world = chunk.getWorld();
		BlockPos pos = PostGenEffectUtils.getVolcanoTop(chunk, top);
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), event, SoundCategory.AMBIENT, 3, 1);
	}
}
