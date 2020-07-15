package com.eleksploded.lavadynamics.arrow;

import com.eleksploded.lavadynamics.LavaDynamics;
import com.eleksploded.lavadynamics.Volcano;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityVolcanoArrow extends EntityArrow {

	public EntityVolcanoArrow(World world) {
		super(world);
	}
	
	public EntityVolcanoArrow(World world, EntityLivingBase shooter) {
		super(world, shooter);
	}

	@Override
	protected ItemStack getArrowStack() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void onHit(RayTraceResult raytraceResultIn)
    {
		if(shootingEntity == null || !(this.shootingEntity instanceof EntityPlayer)) {
			super.onHit(raytraceResultIn);
			return;
		}
		
        Entity entity = raytraceResultIn.entityHit;
        EntityPlayer player = (EntityPlayer) shootingEntity;
        
        if(entity == null) {
        	if(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null) {
        		BlockPos blockpos = raytraceResultIn.getBlockPos();
                world.setBlockState(blockpos, LavaDynamics.VolcanoBlock.getDefaultState());
                Chunk chunk = world.getChunk(blockpos);
                Volcano.genVolcano(chunk, world);
                this.setDead();
        	} else {
        		super.onHit(raytraceResultIn);	
        	}
        } else {
        	super.onHit(raytraceResultIn);
        }
    }
}