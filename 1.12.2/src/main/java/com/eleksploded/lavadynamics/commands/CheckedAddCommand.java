package com.eleksploded.lavadynamics.commands;

import com.eleksploded.lavadynamics.storage.StorageManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CheckedAddCommand extends CommandBase{

	@Override
	public String getName() {
		return "ldaddchunk";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "ldaddchunk ChunkX chunkZ";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 2){
			throw new WrongUsageException(getUsage(sender), new Object[0]);
		}
		
		try{
			StorageManager.getCheckedStorage(sender.getEntityWorld().provider.getDimension()).addChecked(sender.getEntityWorld().getChunk(Integer.valueOf(args[0]), Integer.valueOf(args[1])));
			sender.sendMessage(new TextComponentString("Added chunk [" + Integer.valueOf(args[0]) + "," + Integer.valueOf(args[1]) + "]"));
		} catch(NumberFormatException e){
			throw new WrongUsageException(getUsage(sender), new Object[0]);
		}
	}
	
}
