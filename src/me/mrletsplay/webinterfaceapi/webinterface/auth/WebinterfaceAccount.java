package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.util.List;

import me.mrletsplay.mrcore.permission.Permissible;
import me.mrletsplay.mrcore.permission.Permission;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class WebinterfaceAccount implements Permissible {

	private String id, email;
	private List<WebinterfaceAccountConnection> connections;
	private List<Permission> permissions;
	
	public WebinterfaceAccount(String id, String email, List<WebinterfaceAccountConnection> connections, List<Permission> permissions) {
		this.id = id;
		this.email = email;
		this.connections = connections;
		this.permissions = permissions;
	}
	
	public String getID() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getAvatarUrl() {
		return connections.isEmpty() ? null : connections.get(0).getUserAvatar();
	}
	
	public String getName() {
		return connections.isEmpty() ? null : connections.get(0).getUserName();
	}
	
	public void addConnection(WebinterfaceAccountConnection connection) {
		this.connections.add(connection);
		Webinterface.getAccountStorage().storeAccount(this);
	}
	
	public List<WebinterfaceAccountConnection> getConnections() {
		return connections;
	}
	
	public WebinterfaceAccountConnection getConnection(String authMethod) {
		return connections.stream().filter(c -> c.getAuthMethod().equals(authMethod)).findFirst().orElse(null);
	}

	@Override
	public void addPermission(Permission permission) {
		permissions.add(permission);
	}

	@Override
	public void removePermission(Permission permission) {
		permissions.remove(permission);
	}

	@Override
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public List<Permission> getPermissions() {
		return permissions;
	}
	
}
