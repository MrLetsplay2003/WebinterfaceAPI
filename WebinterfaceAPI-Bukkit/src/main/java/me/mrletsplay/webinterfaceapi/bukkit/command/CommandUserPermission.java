package me.mrletsplay.webinterfaceapi.bukkit.command;

import me.mrletsplay.mrcore.bukkitimpl.command.BukkitCommand;
import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;

public class CommandUserPermission extends BukkitCommand {
	
	public CommandUserPermission() {
		super("permission");
		
		setDescription("Manage permissions for users on the webinterface");
		addSubCommand(new CommandUserPermissionAdd());
	}
	
	@Override
	public void action(CommandInvokedEvent event) {
		sendCommandInfo(event.getSender());
	}

}
