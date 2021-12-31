package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.RawValue;

public class ReloadPageAction implements WebinterfaceAction {
	
	private static final ReloadPageAction
		RELOAD = new ReloadPageAction(),
		FORCE_RELOAD = new ReloadPageAction(true);
	
	private boolean forceReload;
	private int delay;
	
	@Deprecated
	public ReloadPageAction(boolean forceReload, int delayMillis) {
		this.forceReload = forceReload;
		this.delay = delayMillis;
	}

	@Deprecated
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
	
	public static ReloadPageAction reload() {
		return RELOAD;
	}
	
	public static ReloadPageAction forceReload() {
		return FORCE_RELOAD;
	}
	
	public static ReloadPageAction delayed(boolean forceReload, int delayMillis) {
		return new ReloadPageAction(forceReload, delayMillis);
	}
	
	public static ReloadPageAction delayed(int delayMillis) {
		return delayed(false, delayMillis);
	}

}
