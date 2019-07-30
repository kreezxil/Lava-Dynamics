package com.eleksploded.lavadynamics.commands;

import com.eleksploded.lavadynamics.postgen.IPostGenEffect;
import com.eleksploded.lavadynamics.postgen.PostGenEffectRegistry;
import com.eleksploded.lavadynamics.storage.StorageManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;

public class ForcePostGenEffect extends CommandBase {

	@Override
	public String getName() {
		return "forcevolcanoeffect";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/forcevolcanoeffect [effectName]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Chunk chunk = sender.getEntityWorld().getChunkFromBlockCoords(sender.getPosition());
		System.out.println("r" + chunk);

		if(!StorageManager.getVolcanoStorage(sender.getEntityWorld().provider.getDimension()).isVolcano(chunk)){
			sender.sendMessage(new TextComponentString("Chunk does not contain a volcano. Please run this in a chunk that contains a volcamo."));
		} else {
			int top = StorageManager.getVolcanoStorage(sender.getEntityWorld().provider.getDimension()).getTop(chunk);
			if(args.length == 0) {
				PostGenEffectRegistry.runEffect(chunk, top);
			} else if(args.length == 1){
				IPostGenEffect effect = PostGenEffectRegistry.getByName(args[0]);
				if(effect != null){
					effect.execute(chunk, top);
				} else {
					sender.sendMessage(new TextComponentString("No effect by the name " + args[0]));
				}
			} else {
				throw new WrongUsageException(getUsage(sender), new Object[0]);
			}
		}
	}

}
