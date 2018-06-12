/*package com.kreezcraft.lavadynamics.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

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

public class CommandDimension extends CommandBase {

	public CommandDimension() {
		aliases = Lists.newArrayList(LavaDynamics.MODID, "dim", "dimension","world");
	}

	private final List<String> aliases;

	@Override
	@Nonnull
	public String getName() {
		return "dim";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "dim <id> <true|false>\n" + "    allows or disables lava dynamics\n"
				+ "    for the dimension given. 0 is overworld, 1 is Then End, -1 is the\n"
				+ "    Nether. Any other ID from another mod for a dimension is cool too!";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return aliases;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
			throws CommandException {
		String dim,truth;
		
		if (args.length < 2) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		
		dim = args[0];
		truth = args[1];

		int dimension = Integer.parseInt(dim);
		
		List<String> truths = Lists.newArrayList("true", "false");

		if (dimension < -99999 || dimension > 99999) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		if (!truths.contains(truth)) {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		if (truth.equalsIgnoreCase("true")) {
			int[] intList = LavaConfig.dimensions.dimsToAllow.clone();
			for(int i=0;i<intList.length;i++) {
				if(intList[i]==dimension) {
					sender.sendMessage(new TextComponentString("Dimension id "+dimension+" already allowed."));
					return;
				}
			}
			intList = Arrays.copyOf(intList, intList.length+1);
			intList[intList.length-1] = dimension;
			
			LavaConfig.dimensions.dimsToAllow = intList;
		} else {
			//because of the array check above this will be false
			int[] intList = LavaConfig.dimensions.dimsToAllow.clone();
			List<int[]> temp = Arrays.asList(intList);
			
			if(temp.contains(dimension)) {
				temp.remove(dimension);
			} else {
				sender.sendMessage(new TextComponentString("Dimension id "+dimension+" already disallowed."));
			}
			
			Integer[] ints = temp.toArray(new Integer[temp.size()]);
			int[] whatIneed = ArrayUtils.toPrimitive(ints);
			LavaConfig.dimensions.dimsToAllow = whatIneed;
		}

		if (LavaConfig.cfg.hasChanged())

		{
			sender.sendMessage(new TextComponentString("LavaDynamics for " + dimension + " set to " + truth));
			sender.sendMessage(new TextComponentString("Config updated"));
			LavaConfig.cfg.save();
		} else {
			sender.sendMessage(new TextComponentString("Config not updated"));
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