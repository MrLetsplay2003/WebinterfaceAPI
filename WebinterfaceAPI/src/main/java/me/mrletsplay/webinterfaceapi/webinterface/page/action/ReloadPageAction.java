package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.JSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;

public class ReloadPageAction implements Action {

	private static final ReloadPageAction
		RELOAD = new ReloadPageAction(false, 0),
		FORCE_RELOAD = new ReloadPageAction(true, 0);

	private boolean forceReload;
	private int delay;

	private ReloadPageAction(boolean forceReload, int delayMillis) {
		this.forceReload = forceReload;
		this.delay = delayMillis;
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
		ObjectValue o = ActionValue.object();
		o.put("forceReload", () -> Boolean.toString(forceReload));
		o.put("delay", () -> Integer.toString(delay));
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
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
