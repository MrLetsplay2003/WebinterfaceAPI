package me.mrletsplay.webinterfaceapi.webinterface.page.action;

public class WebinterfaceRequestEvent {

	private String target, method;
	private Object data;
	
	public WebinterfaceRequestEvent(String target, String method, Object data) {
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
	
	public Object getRequestData() {
		return data;
	}
	
}
