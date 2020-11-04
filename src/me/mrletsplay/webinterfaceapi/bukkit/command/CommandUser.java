package me.mrletsplay.webinterfaceapi.bukkit.command;

import me.mrletsplay.mrcore.bukkitimpl.command.BukkitCommand;
import me.mrletsplay.mrcore.command.CommandInvokedEvent;

public class CommandUser extends BukkitCommand {
	
	public CommandUser() {
		super("user");
		
		addSubCommand(new CommandUserCreate());
	}

	@Override
	public void action(CommandInvokedEvent event) {
		sendCommandInfo(event.getSender());
	}
	
}
