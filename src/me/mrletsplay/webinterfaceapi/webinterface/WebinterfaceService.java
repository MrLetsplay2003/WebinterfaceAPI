package me.mrletsplay.webinterfaceapi.webinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebinterfaceService {
	
	public static final String
		EVENT_PRE_INIT = "pre-init",
		EVENT_POST_INIT = "post-init",
		EVENT_PRE_START = "pre-start",
		EVENT_POST_START = "post-start";
	
	private Map<String, List<Runnable>> listeners;
	private Set<String> fired;
	
	public WebinterfaceService() {
		this.listeners = new HashMap<>();
		this.fired = new HashSet<>();
	}
	
	public void registerListener(String event, Runnable listener) {
		if(fired.contains(event)) {
			listener.run();
			return;
		}
		
		List<Runnable> ls = listeners.getOrDefault(event, new ArrayList<>());
		ls.add(listener);
		listeners.put(event, ls);
	}
	
	protected void fire(String event) {
		fired.add(event);
		List<Runnable> ls = listeners.remove(event);
		if(ls != null) ls.forEach(Runnable::run);
	}
	
	public WebinterfaceService onPreInitialize(Runnable listener) {
		registerListener(EVENT_PRE_INIT, listener);
		return this;
	}
	
	public WebinterfaceService onPostInitialize(Runnable listener) {
		registerListener(EVENT_POST_INIT, listener);
		return this;
	}
	
	public WebinterfaceService onPreStart(Runnable listener) {
		registerListener(EVENT_PRE_START, listener);
		return this;
	}
	
	public WebinterfaceService onPostStart(Runnable listener) {
		registerListener(EVENT_POST_START, listener);
		return this;
	}
	
}
