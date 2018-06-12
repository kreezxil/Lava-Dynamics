/*package com.kreezcraft.lavadynamics.commands.addCommands;

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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import scala.actors.threadpool.Arrays;

public class CommandConfirm extends CommandBase {

	public CommandConfirm() {
		aliases = Lists.newArrayList(LavaDynamics.MODID, "confirm","accept");
	}

	private final List<String> aliases;

	@Override
	@Nonnull
	public String getName() {
		return "confirm";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return getName()+" to finalize the source to destination conversion combination to the configs.";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {

		if(LavaDynamics.source == null) {
			sender.sendMessage(new TextComponentString("Source item/block not set"));
		} else if(LavaDynamics.destination == null) {
			sender.sendMessage(new TextComponentString("Destination item/block not set"));
		} else {
//			String combo = LavaDynamics.source + "@" + LavaDynamics.destination;
//			List<String> combos = Arrays.asList(LavaConfig.conversions.getStringList());
//			String[] map = null;
//			combos.add(combo);
//			combos.toArray(map);
//			LavaConfig.conversions.set(map);
//			sender.sendMessage(new TextComponentString(combo + "saved to conversions in config"));
		}

		if(LavaConfig.cfg.hasChanged()) {
			LavaConfig.cfg.save();
			if(LavaConfig.generalConfig.debugMode) {
				sender.sendMessage(new TextComponentString("Mapping config updated"));
			}
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
*/