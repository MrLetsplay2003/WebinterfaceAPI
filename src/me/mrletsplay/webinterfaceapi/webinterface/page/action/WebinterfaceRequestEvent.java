package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceRequestEvent {

	private WebinterfaceSession session;
	
	private String
		target,
		method;
	
	private JSONObject data;
	
	public WebinterfaceRequestEvent(WebinterfaceSession session, String target, String method, JSONObject data) {
		this.session = session;
		this.target = target;
		this.method = method;
		this.data = data;
	}
	
	public WebinterfaceSession getSession() {
		return session;
	}
	
	public WebinterfaceAccount getAccount() {
		return session.getAccount();
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
