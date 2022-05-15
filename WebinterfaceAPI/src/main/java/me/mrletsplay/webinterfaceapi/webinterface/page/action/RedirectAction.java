package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.JSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;

public class RedirectAction implements Action {

	private ActionValue url;

	private RedirectAction(ActionValue url) {
		this.url = url;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.redirect";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = ActionValue.object();
		o.put("url", url);
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static RedirectAction to(ActionValue url) {
		return new RedirectAction(url);
	}

	public static RedirectAction to(String url) {
		return new RedirectAction(ActionValue.string(url));
	}

}
