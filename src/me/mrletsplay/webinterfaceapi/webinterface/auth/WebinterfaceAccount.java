package me.mrletsplay.webinterfaceapi.webinterface.auth;

import java.util.List;

import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class WebinterfaceAccount {

	private String id, email;
	private List<WebinterfaceAccountConnection> data;
	
	public WebinterfaceAccount(String id, String email, List<WebinterfaceAccountConnection> data) {
		this.id = id;
		this.email = email;
		this.data = data;
	}
	
	public String getID() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getAvatarUrl() {
		return data.isEmpty() ? null : data.get(0).getUserAvatar();
	}
	
	public String getName() {
		return data.isEmpty() ? null : data.get(0).getUserName();
	}
	
	public void addData(WebinterfaceAccountConnection data) {
		this.data.add(data);
		Webinterface.getAccountStorage().storeAccount(this);
	}
	
	public List<WebinterfaceAccountConnection> getData() {
		return data;
	}
	
	public WebinterfaceAccountConnection getData(String authMethod) {
		return data.stream().filter(d -> d.getAuthMethod().equals(authMethod)).findFirst().orElse(null);
	}
	
}
