package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.simplehttpserver.http.websocket.WebSocketConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.session.Session;

public class ActionEvent {

	private Session session;

	private WebSocketConnection webSocketConnection;

	private String
		target,
		method;

	private JSONObject data;

	public ActionEvent(WebSocketConnection webSocketConnection, Session session, String target, String method, JSONObject data) {
		this.webSocketConnection = webSocketConnection;
		this.session = session;
		this.target = target;
		this.method = method;
		this.data = data;
	}

	public boolean isFromWebSocket() {
		return webSocketConnection != null;
	}

	public WebSocketConnection getWebSocketConnection() {
		return webSocketConnection;
	}

	public Session getSession() {
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
