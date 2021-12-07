package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class WebinterfaceActionHandlers {
	
	public static WebinterfaceResponse handle(WebinterfaceRequestEvent event) {
		if(event.getRequestTarget() == null || event.getRequestMethod() == null)
			return WebinterfaceResponse.error("No request target or method");
		
		for(WebinterfaceActionHandler h : Webinterface.getActionHandlers()) {
			try {
				WebinterfaceResponse r = h.handle(event);
				if(r != null) return r;
			}catch(Exception ex) {
				Webinterface.getLogger().debug("Failed to handle request, error in handler", ex);
				return WebinterfaceResponse.error("Failed to handle request, error in handler");
			}
		}
		return WebinterfaceResponse.error("No handler available");
	}

}
