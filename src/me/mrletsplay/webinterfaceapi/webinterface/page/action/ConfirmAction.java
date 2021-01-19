package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;

public class ConfirmAction implements WebinterfaceAction {
	
	private WebinterfaceAction action;
	
	public ConfirmAction(WebinterfaceAction action) {
		this.action = action;
	}
	
	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.confirm";
	}
	
	@Override
	public JSONObject getParameters() {
		JSONObject o = new JSONObject();
		o.put("actionName", action.getHandlerName());
		o.put("actionParameters", action.getParameters());
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
