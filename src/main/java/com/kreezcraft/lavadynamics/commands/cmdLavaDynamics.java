package com.kreezcraft.lavadynamics.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class cmdLavaDynamics extends CommandTreeBase {

//	@Override
//	public void addSubcommand(ICommand command) {
//		// TODO Auto-generated method stub
//		super.addSubcommand(command);
//	}

	
	@Override
	public String getName() {
		return "LavaDynamics";
	}

	public cmdLavaDynamics() {
		super();
		//addSubcommand(new cmdAdd());
		addSubcommand(new CommandDebug());
	//	addSubcommand(new CommandDimension());
	//	addSubcommand(new CommandDisplay());
	}

	@Override
	public String getUsage(ICommandSender sender) {
		//return "/LavaDynamics <add|del|dim|sho|dbg>\n"+
//		return "/LavaDynamics <dim|sho|dbg>\n"+
//	           "   add - add a combo to the conversion list\n"+
//	           "   rem - remove a combo from the conversion list\n"+
//	           "   dim - contol dimension access\n"+
//	           "   sho - show the config settings\n"+
//	           "   dbg - manage debug messages";
		return "/LavaDynamics debug <true|false>\n   prints messages to console";
	}
}