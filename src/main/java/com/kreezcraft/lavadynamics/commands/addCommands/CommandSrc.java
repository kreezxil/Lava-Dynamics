package com.kreezcraft.lavadynamics.commands.addCommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kreezcraft.lavadynamics.LavaDynamics;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandSrc extends CommandBase {

	public CommandSrc() {
		aliases = Lists.newArrayList(LavaDynamics.MODID, "src","source");
	}

	private final List<String> aliases;

	@Override
	@Nonnull
	public String getName() {
		return "src";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return getName()+" - the item in the hand will be the source item";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		EntityPlayer player;
		try {
			player = getCommandSenderAsPlayer(sender);
		} catch (Exception e) {
			return;
		}
		
		if(player.getHeldItemMainhand().isEmpty()) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
		} else {
			LavaDynamics.source = player.getHeldItemMainhand();
			sender.sendMessage(new TextComponentString(((ItemStack) LavaDynamics.source).getDisplayName()+"set as source item"));
		}
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
		if (server.getPlayerList().getOppedPlayers().getGameProfileFromName(sender.getName()) != null) {
			return true; // ops can use the command
		}
		if(server.isSinglePlayer()) {
			return true; //single player can use the command in creative
		}
	return false;
	}

}
