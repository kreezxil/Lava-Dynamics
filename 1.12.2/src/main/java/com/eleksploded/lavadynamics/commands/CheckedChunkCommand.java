package com.eleksploded.lavadynamics.commands;

import com.eleksploded.lavadynamics.storage.CheckedCap;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;

public class CheckedChunkCommand extends CommandBase {

	@Override
	public String getName() {
		return "ldcheckedchunk";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "LDCheckedChunk";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Chunk chunk = sender.getEntityWorld().getChunk(sender.getPosition());
		if(chunk.getCapability(CheckedCap.checkedCap, null).isChecked()){
			sender.sendMessage(new TextComponentString("Chunk [" + chunk.x + "," + chunk.z + "] has been checked"));
		} else {
			sender.sendMessage(new TextComponentString("Chunk [" + chunk.x + "," + chunk.z + "] has not been checked"));
		}
	}
	
}
