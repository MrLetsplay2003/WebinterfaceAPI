package me.mrletsplay.webinterfaceapi.bukkit.command;

import me.mrletsplay.mrcore.bukkitimpl.command.BukkitCommand;
import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.auth.impl.PasswordAuth;

public class CommandUserCreate extends BukkitCommand {

	public CommandUserCreate() {
		super("create");

		setDescription("Creates a user with the password auth method");
		setUsage("wiapi user create <username> <password>");
	}

	@Override
	public void action(CommandInvokedEvent event) {
		if(event.getArguments().length != 2) {
			sendCommandInfo(event.getSender());
			return;
		}

		String username = event.getArguments()[0];
		String password = event.getArguments()[1];

		if(!PasswordAuth.isValidUsername(username)) {
			event.getSender().sendMessage("§cUsername contains invalid characters or is too long/short");
			return;
		}

		if(Webinterface.getAccountStorage().getAccountByConnectionSpecificID(PasswordAuth.ID, username) != null) {
			event.getSender().sendMessage("§cA user with that username already exists");
			return;
		}

		Account acc = Webinterface.getAccountStorage().createAccount();
		acc.addConnection(new AccountConnection(PasswordAuth.ID, username, username, null, null));
		Webinterface.getCredentialsStorage().storeCredentials(username, password);
		event.getSender().sendMessage("§aUser created");
	}

}
