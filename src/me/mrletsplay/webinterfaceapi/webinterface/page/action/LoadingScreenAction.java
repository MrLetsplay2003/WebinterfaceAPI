package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;

public class LoadingScreenAction implements WebinterfaceAction {
	
	private static final LoadingScreenAction
		SHOW = new LoadingScreenAction(true),
		HIDE = new LoadingScreenAction(false);
	
	private boolean show;
	
	private LoadingScreenAction(boolean show) {
		this.show = show;
	}

	@Override
	public String getHandlerName() {
		return show ? "WebinterfaceBaseActions.showLoadingScreen" : "WebinterfaceBaseActions.hideLoadingScreen";
	}
	
	@Override
	public ObjectValue getParameters() {
		return new ObjectValue();
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}
	
	public static LoadingScreenAction show() {
		return SHOW;
	}
	
	public static LoadingScreenAction hide() {
		return HIDE;
	}

}