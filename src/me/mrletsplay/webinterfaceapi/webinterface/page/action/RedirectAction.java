package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;

public class RedirectAction implements WebinterfaceAction {

	private WebinterfaceActionValue url;
	
	public RedirectAction(WebinterfaceActionValue url) {
		this.url = url;
	}
	
	public RedirectAction(String url) {
		this(new StringValue(url));
	}
	
	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.redirect";
	}
	
	@Override
	public ObjectValue getParameters() {
		ObjectValue o = new ObjectValue();
		o.put("url", url);
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
