package me.mrletsplay.webinterfaceapi.bukkit.command;

import org.bukkit.command.CommandSender;

import me.mrletsplay.mrcore.bukkitimpl.command.BukkitCommand;
import me.mrletsplay.mrcore.bukkitimpl.command.BukkitCommandSender;
import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.Account;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class CommandUserPermissionAdd extends BukkitCommand {
	
	public CommandUserPermissionAdd() {
		super("add");
		
		setDescription("Add a permission to a user on the webinterface");
		setUsage("wiapi user permission add <account id | connection name:user id> <permission>");
	}
	
	@Override
	public void action(CommandInvokedEvent event) {
		if(event.getArguments().length != 2) {
			sendCommandInfo(event.getSender());
			return;
		}
		
		String userID = event.getArguments()[0];
		String permission = event.getArguments()[1];
		
		Account account;
		if(userID.contains(":")) {
			String m = userID.split(":", 2)[0];
			String i = userID.split(":", 2)[1];
			account = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(m, i);
		}else {
			account = Webinterface.getAccountStorage().getAccountByID(userID);
		}
		
		if(account == null) {
			event.getSender().sendMessage("That account doesn't exist");
			return;
		}
		
		if(account.hasPermissionExactly(permission)) {
			event.getSender().sendMessage("That account already has the specified permission");
			return;
		}
		
		account.addPermission(permission);
		CommandSender s = ((BukkitCommandSender) event.getSender()).getBukkitSender();
		s.spigot().sendMessage(new ComponentBuilder()
				.append("Granted permission ").color(ChatColor.GREEN)
				.append(permission).color(ChatColor.GOLD)
				.append(" to user ").color(ChatColor.GREEN)
				.append(account.getName()).event(new HoverEvent(Action.SHOW_TEXT, CommandWebinterfaceAPI.createHoverText(account))).color(ChatColor.GOLD).create());
	}

}
