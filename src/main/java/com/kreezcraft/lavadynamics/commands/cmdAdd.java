/*package com.kreezcraft.lavadynamics.commands;

import com.kreezcraft.lavadynamics.commands.addCommands.CommandConfirm;
import com.kreezcraft.lavadynamics.commands.addCommands.CommandDst;
import com.kreezcraft.lavadynamics.commands.addCommands.CommandSrc;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class cmdAdd extends CommandTreeBase {

	@Override
	public String getName() {
		return "add";
	}

	public cmdAdd() {
		super();
		addSubcommand(new CommandSrc());
		addSubcommand(new CommandDst());
		addSubcommand(new CommandConfirm());
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "add src <item in hand>\n"+
			   "    dst <item in hand>\n"+
			   "    confirm - commit the combo conversion.";
	}
}*/