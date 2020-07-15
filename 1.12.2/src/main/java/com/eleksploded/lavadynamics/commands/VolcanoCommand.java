package com.eleksploded.lavadynamics.commands;

import com.eleksploded.lavadynamics.Volcano;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;

public class VolcanoCommand extends CommandBase {

	@Override
	public String getName() {
		return "spawnvolcano";
	}
	
	public int getRequiredPermissionLevel()
    {
        return 4;
    }

	@Override
	public String getUsage(ICommandSender sender) {
		return "/spawnvolcano [TileEntityBypass]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {		
		Chunk chunk = sender.getEntityWorld().getChunk(sender.getPosition());
		boolean bypass;
		if(args.length == 1){
			try {
				bypass = CommandBase.parseBoolean(args[0]);
			} catch (CommandException e) {
				throw new WrongUsageException(getUsage(sender), "true","false","0","1");
			}
		} else if(args == null || args.length == 0) {
			bypass = false;
		} else {
			throw new WrongUsageException(getUsage(sender), "true","false","0","1");
		}
		
		if(Volcano.hasTileEntity(sender.getEntityWorld(), chunk) && !bypass){
			sender.sendMessage(new TextComponentString("Chunk contains tile entity so a volcano was not generated"));
			return;
		}
			
		Volcano.genVolcano(chunk, sender.getEntityWorld());
	}
}
