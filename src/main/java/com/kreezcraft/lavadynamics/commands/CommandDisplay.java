/*package com.kreezcraft.lavadynamics.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kreezcraft.lavadynamics.LavaConfig;
import com.kreezcraft.lavadynamics.LavaDynamics;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandDisplay extends CommandBase {

	public CommandDisplay() {
		aliases = Lists.newArrayList(LavaDynamics.MODID, "display", "show", "view");
	}

	private final List<String> aliases;

	@Override
	@Nonnull
	public String getName() {
		return "show";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "show <all|dims|map>\n    show the config or parts thereof";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	private void printMsg(ICommandSender sender, String msg) {
		sender.sendMessage(new TextComponentString(msg));
	}

	private void showWorlds(ICommandSender sender) {
		printMsg(sender, "Allowed Dimensions - Dims not in list are not allowed");
		printMsg(sender, "=======================");
		printMsg(sender, LavaConfig.dimensions.dimsToAllow.toString());
		printMsg(sender, " ");
	}


	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		boolean theTruth;
		if (args.length < 1) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		String action = args[0];

//		if (action.equalsIgnoreCase("all")) {
//			showWorlds(sender);
//			showGeneral(sender);
//		} else if (action.equalsIgnoreCase("dims")) {
//			showWorlds(sender);
//		} else if (action.equalsIgnoreCase("mapping")) {
//			showMapping(sender);
//		} else {
//			sender.sendMessage(new TextComponentString(getUsage(sender)));
//			return;
//		}

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
		return true;
	}

}
*/