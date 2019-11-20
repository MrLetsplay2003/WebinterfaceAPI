package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.permission.Permissible;
import me.mrletsplay.mrcore.permission.Permission;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class WebinterfaceAccount implements Permissible {

	private String id;
	private List<WebinterfaceAccountConnection> connections;
	private List<Permission> permissions;
	
	public WebinterfaceAccount(String id, List<WebinterfaceAccountConnection> connections, List<Permission> permissions) {
		this.id = id;
		this.connections = connections;
		this.permissions = permissions;
	}
	
	public String getID() {
		return id;
	}
	
	public List<String> getEmails() {
		return connections.stream()
				.map(c -> c.getUserEmail())
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
	public String getPrimaryEmail() {
		return getEmails().get(0);
	}
	
	public String getAvatarUrl() {
		return connections.stream()
					.map(c -> c.getUserAvatar())
					.filter(Objects::nonNull)
					.findFirst().orElse(null);
	}
	
	public String getName() {
		return connections.stream()
				.map(c -> c.getUserName())
				.filter(Objects::nonNull)
				.findFirst().orElse(null);
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
