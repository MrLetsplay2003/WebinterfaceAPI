package me.mrletsplay.webinterfaceapi.bukkit.command;

import me.mrletsplay.mrcore.bukkitimpl.command.BukkitCommand;
import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.webinterfaceapi.webinterface.auth.Account;
import net.md_5.bungee.api.chat.hover.content.Text;

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
	
	public static Text createHoverText(Account account) {
		return new Text("§7Account Name: §6" + account.getName() + "\n§7Account ID: §6" + account.getID());
	}
	
}
