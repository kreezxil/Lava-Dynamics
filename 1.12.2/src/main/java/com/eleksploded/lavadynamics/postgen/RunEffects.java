package com.eleksploded.lavadynamics.postgen;

import java.util.Random;

import com.eleksploded.lavadynamics.LavaConfig;
import com.eleksploded.lavadynamics.Reference;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Reference.MODID, value = Side.SERVER)
public class RunEffects {
	static int timer = 0;

	@SubscribeEvent
	public static void load(WorldTickEvent event){
		if(event.world.playerEntities.size() == 0) return;
		if(event.world.isRemote) return;
		
		if(timer != 0){
			timer--;
			return;
		}
		
		Random rand = new Random();
		if(rand.nextInt(999)+1 <= LavaConfig.postgen.chance){
			/*VolcanoStorage storage = StorageManager.getVolcanoStorage(event.world.provider.getDimension());
			if(storage != null){
				int num = storage.getNum();
				if(num != 0){
					Chunk chunk = storage.get(rand.nextInt(num)+1, event.world);
					PostGenEffectRegistry.runEffect(chunk, storage.getTop(chunk));
					timer = (int)Math.floor(LavaConfig.postgen.effectTime * 1200);
				}
			}*/
		}
	}
}