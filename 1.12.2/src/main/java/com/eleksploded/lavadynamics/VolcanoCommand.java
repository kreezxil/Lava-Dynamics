package com.eleksploded.lavadynamics;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class VolcanoCommand extends CommandBase {

	@Override
	public String getName() {
		return "spawnvolcano";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {		
		Volcano.genVolcano(sender.getEntityWorld().getChunkFromBlockCoords(sender.getPosition()), sender.getEntityWorld());
	}
	
}
