/*package com.kreezcraft.lavadynamics.commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kreezcraft.lavadynamics.LavaConfig;
import com.kreezcraft.lavadynamics.LavaDynamics;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandDebug extends CommandBase {

	public CommandDebug() {
		aliases = Lists.newArrayList(LavaDynamics.MODID, "debug","inform");
	}
	
	private final List<String> aliases;
	

	@Override
	@Nonnull
	public String getName() {
		return "debug";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "debug <true|false>\n    turn on debugmode or not";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		boolean theTruth;
		if (args.length < 1) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		String truth = args[0];

		if (truth.equalsIgnoreCase("true")) {
			theTruth = true;
		} else if (truth.equalsIgnoreCase("false")) {
			theTruth = false;
		} else {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		
		sender.sendMessage(new TextComponentString("LavaDynamics Debug Mode is "+theTruth));
		LavaConfig.generalConfig.debugMode = theTruth;
		sender.sendMessage(new TextComponentString("Config updated"));
		LavaConfig.cfg.save();

		return;
	
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		return Collections.emptyList();
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if(server.getPlayerList().getOppedPlayers().getGameProfileFromName(sender.getName()) != null) {
			return true; //ops can use the command
		}
		if(server.isSinglePlayer()) {
			return true; //single player can use the command in creative
		}
		return false;
	}

}
*/