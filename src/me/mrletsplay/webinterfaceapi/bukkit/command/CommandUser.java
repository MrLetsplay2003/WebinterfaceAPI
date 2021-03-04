package me.mrletsplay.webinterfaceapi.bukkit.command;

import me.mrletsplay.mrcore.bukkitimpl.command.BukkitCommand;
import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;

public class CommandUser extends BukkitCommand {
	
	public CommandUser() {
		super("user");
		
		addSubCommand(new CommandUserCreate());
		addSubCommand(new CommandUserPermission());
	}

	@Override
	public void action(CommandInvokedEvent event) {
		sendCommandInfo(event.getSender());
	}
	
}
