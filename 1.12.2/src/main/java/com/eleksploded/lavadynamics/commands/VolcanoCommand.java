package com.eleksploded.lavadynamics.commands;

import com.eleksploded.lavadynamics.Volcano;
import com.eleksploded.lavadynamics.storage.StorageManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;

public class VolcanoCommand extends CommandBase {

	@Override
	public String getName() {
		return "spawnvolcano";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/spawnvolcano [TileEntityBypass]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {		
		Chunk chunk = sender.getEntityWorld().getChunkFromBlockCoords(sender.getPosition());
		boolean bypass;
		if(args.length == 1){
			if(args[0] == "true"){
				bypass = true;
			} else {
				bypass = false;
			}
		} else {
			bypass = false;
		}
		
		if(Volcano.hasTileEntity(sender.getEntityWorld(), chunk)){
			sender.sendMessage(new TextComponentString("Chunk contains tile entity so a volcano was not generated"));
			if(!bypass){
				return;
			}
		}
			
		Volcano.genVolcano(chunk, sender.getEntityWorld());
	}
}
