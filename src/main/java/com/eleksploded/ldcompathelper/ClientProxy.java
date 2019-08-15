package com.eleksploded.ldcompathelper;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void register(){
		ClientRegistry.registerKeyBinding(LDCompatHelper.key);
	}
}
