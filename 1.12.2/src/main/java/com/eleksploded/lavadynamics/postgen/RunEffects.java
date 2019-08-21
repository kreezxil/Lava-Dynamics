package com.eleksploded.lavadynamics.postgen;

import java.util.Random;

import com.eleksploded.lavadynamics.LavaConfig;
import com.eleksploded.lavadynamics.storage.StorageManager;
import com.eleksploded.lavadynamics.storage.VolcanoStorage;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@Mod.EventBusSubscriber
public class RunEffects {
	static int timer = 0;

	@SubscribeEvent
	public static void load(WorldTickEvent event){
		if(event.world.playerEntities.size() == 0){return;}
		
		if(timer != 0){
			timer--;
			return;
		}
		
		Random rand = new Random();
		if(rand.nextInt(999)+1 <= LavaConfig.postgen.chance){
			VolcanoStorage storage = StorageManager.getVolcanoStorage(event.world.provider.getDimension());
			if(storage != null){
				if(storage.getList().size() != 0){
					Chunk chunk = storage.getList().get(rand.nextInt(storage.getList().size()));
					PostGenEffectRegistry.runEffect(chunk, storage.getTop(chunk));
					timer = (int)Math.round(LavaConfig.postgen.effectTime * 1200);
				}
			}
		}
	}
}