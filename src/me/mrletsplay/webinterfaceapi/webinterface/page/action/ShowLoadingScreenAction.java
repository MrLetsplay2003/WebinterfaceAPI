package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;

/**
 * Use {@link LoadingScreenAction#show()} instead
 * @author MrLetsplay2003
 */
public class ShowLoadingScreenAction implements WebinterfaceAction {

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.showLoadingScreen";
	}
	
	@Override
	public ObjectValue getParameters() {
		return new ObjectValue();
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
