package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.RawValue;

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
	public ObjectValue getParameters() {
		ObjectValue o = new ObjectValue();
		o.put("action", new RawValue(action.getHandlerName()));
		o.put("actionParameters", action.getParameters());
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
