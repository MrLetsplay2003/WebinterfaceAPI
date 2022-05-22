package me.mrletsplay.webinterfaceapi.page.action;

import me.mrletsplay.webinterfaceapi.Webinterface;

public class WebinterfaceActionHandlers {

	public static ActionResponse handle(ActionEvent event) {
		if(event.getTarget() == null || event.getMethod() == null)
			return ActionResponse.error("No request target or method");

		for(ActionHandler h : Webinterface.getActionHandlers()) {
			try {
				ActionResponse r = h.handle(event);
				if(r != null) return r;
			}catch(Exception ex) {
				Webinterface.getLogger().error("Failed to handle request, error in handler", ex);
				return ActionResponse.error("Failed to handle request, error in handler");
			}
		}
		return ActionResponse.error("No handler available");
	}

}
