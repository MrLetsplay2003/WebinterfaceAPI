package me.mrletsplay.webinterfaceapi.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.page.element.ElementID;
import me.mrletsplay.webinterfaceapi.page.element.PageElement;

public class SetValueAction implements Action {

	private ElementID elementID;
	private ActionValue value;

	private SetValueAction(ElementID elementID, ActionValue value) {
		this.elementID = elementID;
		this.value = value;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.setValue";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = ActionValue.object();
		o.put("element", ActionValue.string(elementID.get()));
		o.put("value", value);
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static SetValueAction of(ElementID elementID, ActionValue value) {
		elementID.require();
		return new SetValueAction(elementID, value);
	}

	public static SetValueAction of(PageElement element, ActionValue value) {
		return new SetValueAction(element.requireID(), value);
	}

}
