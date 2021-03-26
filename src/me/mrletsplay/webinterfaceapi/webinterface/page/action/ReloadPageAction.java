package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.RawValue;

public class ReloadPageAction implements WebinterfaceAction {
	
	private boolean forceReload;
	private int delay;
	
	public ReloadPageAction(boolean forceReload, int delayMillis) {
		this.forceReload = forceReload;
		this.delay = delayMillis;
	}
	
	public ReloadPageAction(boolean forceReload) {
		this(forceReload, 0);
	}
	
	public ReloadPageAction() {
		this(false);
	}
	
	public void setDelay(int delayMillis) {
		this.delay = delayMillis;
	}
	
	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.reloadPage";
	}
	
	@Override
	public ObjectValue getParameters() {
		ObjectValue o = new ObjectValue();
		o.put("forceReload", new RawValue(Boolean.toString(forceReload)));
		o.put("delay", new RawValue(Integer.toString(delay)));
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
