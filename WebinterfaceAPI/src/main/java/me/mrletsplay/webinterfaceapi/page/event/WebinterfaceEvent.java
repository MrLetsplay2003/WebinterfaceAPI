package me.mrletsplay.webinterfaceapi.page.event;

public class WebinterfaceEvent {
	
	private String target;
	private String eventName;
	private String permission;
	
	public WebinterfaceEvent(String target, String eventName, String permission) {
		this.target = target;
		this.eventName = eventName;
		this.permission = permission;
	}

	public String getTarget() {
		return target;
	}

	public String getEventName() {
		return eventName;
	}

	public String getPermission() {
		return permission;
	}
	
}
