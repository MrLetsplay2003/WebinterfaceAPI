package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceUpdateableElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.list.WebinterfaceElementList;

public class UpdateElementAction implements WebinterfaceAction {

	private String elementID;
	
	@Deprecated
	public UpdateElementAction(String elementID) {
		this.elementID = elementID;
	}

	@Deprecated
	public UpdateElementAction(WebinterfacePageElement element) {
		this(element.getOrGenerateID());
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.updateElement";
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
	
	public static UpdateElementAction of(String elementID) {
		return new UpdateElementAction(elementID);
	}
	
	public static UpdateElementAction of(WebinterfaceUpdateableElement element) {
		return new UpdateElementAction(element);
	}
	
	public static UpdateElementAction of(WebinterfaceElementList<?> element) {
		if(!element.isDynamic()) throw new IllegalArgumentException("Can only update dynamic lists");
		return new UpdateElementAction(element);
	}

}
