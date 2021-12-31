package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;

public class SetValueAction implements WebinterfaceAction {

	private String elementID;
	private WebinterfaceActionValue value;
	
	public SetValueAction(String elementID, WebinterfaceActionValue value) {
		this.elementID = elementID;
		this.value = value;
	}
	
	public SetValueAction(WebinterfacePageElement element, WebinterfaceActionValue value) {
		this(element.getOrGenerateID(), value);
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.setValue";
	}
	
	public ObjectValue getParameters() {
		ObjectValue o = new ObjectValue();
		o.put("element", new StringValue(elementID));
		o.put("value", value);
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
