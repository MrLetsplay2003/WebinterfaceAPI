package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.RawValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;

public class ShowToastAction implements WebinterfaceAction {
	
	private StringValue message;
	private boolean error;
	
	private ShowToastAction(StringValue message, boolean error) {
		this.message = message;
		this.error = error;
	}
	
	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.showToast";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue v = new ObjectValue();
		v.put("message", message);
		v.put("error", new RawValue(String.valueOf(error)));
		return v;
	}

	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.TOAST);
	}
	
	public static ShowToastAction info(StringValue message) {
		return new ShowToastAction(message, false);
	}
	
	public static ShowToastAction info(String message) {
		return new ShowToastAction(new StringValue(message), false);
	}
	
	public static ShowToastAction error(StringValue message) {
		return new ShowToastAction(message, true);
	}
	
	public static ShowToastAction error(String message) {
		return new ShowToastAction(new StringValue(message), true);
	}

}
