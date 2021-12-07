package me.mrletsplay.webinterfaceapi.webinterface.document.websocket;

import java.util.HashSet;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.page.event.WebinterfaceEvent;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebSocketData {
	
	private WebinterfaceSession session;
	private Set<WebinterfaceEvent> subscribedEvents;
	
	public WebSocketData(WebinterfaceSession session) {
		this.session = session;
		this.subscribedEvents = new HashSet<>();
	}
	
	public void setSession(WebinterfaceSession session) {
		this.session = session;
	}
	
	public WebinterfaceSession getSession() {
		return session;
	}
	
	public Set<WebinterfaceEvent> getSubscribedEvents() {
		return subscribedEvents;
	}

}
