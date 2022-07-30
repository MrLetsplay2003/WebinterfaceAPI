package me.mrletsplay.webinterfaceapi.auth;

import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.permission.Permissible;
import me.mrletsplay.mrcore.permission.Permission;
import me.mrletsplay.webinterfaceapi.Webinterface;

public class Account implements Permissible {

	private String id;
	private String username;
	private String avatar;
	private String email;

	public Account(String id, String username, String avatar, String email) {
		this.id = id;
		this.username = username;
		this.avatar = avatar;
		this.email = email;
	}

	public Account(String id, String username) {
		this(id, username, null, null);
	}

	public String getID() {
		return id;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public void addConnection(AccountConnection connection) {
		Webinterface.getAccountStorage().addConnection(id, connection);
		if(avatar == null && connection.getUserAvatar() != null) {
			avatar = connection.getUserAvatar();
			Webinterface.getAccountStorage().updateAccount(this);
		}

		if(email == null && connection.getUserEmail() != null) {
			email = connection.getUserEmail();
			Webinterface.getAccountStorage().updateAccount(this);
		}
	}

	public void removeConnection(AccountConnection connection) {
		removeConnection(connection.getConnectionName());
	}

	public void removeConnection(String connectionName) {
		Webinterface.getAccountStorage().removeConnection(id, connectionName);
	}

	public List<AccountConnection> getConnections() {
		return Webinterface.getAccountStorage().getConnections(id);
	}

	public AccountConnection getConnection(String connectionName) {
		return Webinterface.getAccountStorage().getConnection(id, connectionName);
	}

	@Override
	public void addPermission(Permission permission) {
		Webinterface.getAccountStorage().addPermission(id, permission.getPermission());
	}

	@Override
	public void removePermission(Permission permission) {
		Webinterface.getAccountStorage().removePermission(id, permission.getPermission());
	}

	@Override
	public void setPermissions(List<Permission> permissions) {
		Webinterface.getAccountStorage().setPermissions(id, permissions.stream().map(p -> p.getPermission()).collect(Collectors.toList()));
	}

	@Override
	public List<Permission> getPermissions() {
		return Webinterface.getAccountStorage().getPermissions(id).stream()
			.map(Permission::new)
			.collect(Collectors.toList());
	}

}
