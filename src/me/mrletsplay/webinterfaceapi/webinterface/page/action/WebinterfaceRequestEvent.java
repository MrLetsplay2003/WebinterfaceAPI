package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.mrcore.json.JSONObject;

public class WebinterfaceRequestEvent {

	private String target, method;
	private JSONObject data;
	
	public WebinterfaceRequestEvent(String target, String method, JSONObject data) {
		this.target = target;
		this.method = method;
		this.data = data;
	}
	
	public String getRequestTarget() {
		return target;
	}
	
	public String getRequestMethod() {
		return method;
	}
	
	public JSONObject getRequestData() {
		return data;
	}
	
}
