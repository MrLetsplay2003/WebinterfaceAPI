package me.mrletsplay.webinterfaceapi.webinterface.document.websocket;

import java.util.HashSet;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.page.event.WebinterfaceEvent;
import me.mrletsplay.webinterfaceapi.webinterface.session.Session;

public class WebSocketData {
	
	private Session session;
	private Set<WebinterfaceEvent> subscribedEvents;
	
	public WebSocketData(Session session) {
		this.session = session;
		this.subscribedEvents = new HashSet<>();
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	public Session getSession() {
		return session;
	}
	
	public Set<WebinterfaceEvent> getSubscribedEvents() {
		return subscribedEvents;
	}
	
	public synchronized void subscribe(WebinterfaceEvent event) {
		subscribedEvents.add(event);
	}
	
	public synchronized void unsubscribe(WebinterfaceEvent event) {
		subscribedEvents.remove(event);
	}

}
