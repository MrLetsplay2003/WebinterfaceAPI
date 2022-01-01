package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceUpdateableElement;

public class UpdateElementAction implements WebinterfaceAction {

	private String elementID;
	
	public UpdateElementAction(String elementID) {
		this.elementID = elementID;
	}
	
	public UpdateElementAction(WebinterfaceUpdateableElement element) {
		this(element.getOrGenerateID());
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.updateUpdateableElement";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = new ObjectValue();
		o.put("element", new StringValue(elementID));
		return o;
	}

	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
