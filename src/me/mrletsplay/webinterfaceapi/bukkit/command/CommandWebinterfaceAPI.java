package me.mrletsplay.webinterfaceapi.bukkit.command;

import me.mrletsplay.mrcore.bukkitimpl.command.BukkitCommand;
import me.mrletsplay.mrcore.command.CommandInvokedEvent;

public class CommandWebinterfaceAPI extends BukkitCommand {

	public CommandWebinterfaceAPI() {
		super("webinterfaceapi");
		addAlias("wiapi");
		
		addSubCommand(new CommandUser());
	}

	@Override
	public void action(CommandInvokedEvent event) {
		sendCommandInfo(event.getSender());
	}
	
	

}
