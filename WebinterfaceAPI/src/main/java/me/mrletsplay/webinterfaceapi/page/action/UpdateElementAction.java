package me.mrletsplay.webinterfaceapi.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.page.element.ElementID;
import me.mrletsplay.webinterfaceapi.page.element.Group;
import me.mrletsplay.webinterfaceapi.page.element.UpdateableElement;
import me.mrletsplay.webinterfaceapi.page.element.list.ElementList;

public class UpdateElementAction implements Action {

	private ElementID elementID;

	private UpdateElementAction(ElementID elementID) {
		this.elementID = elementID;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.updateElement";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = ActionValue.object();
		o.put("element", ActionValue.string(elementID.get()));
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static UpdateElementAction of(ElementID elementID) {
		elementID.require();
		return new UpdateElementAction(elementID);
	}

	public static UpdateElementAction of(UpdateableElement element) {
		return new UpdateElementAction(element.requireID());
	}

	public static UpdateElementAction of(ElementList<?> element) {
		return new UpdateElementAction(element.requireID());
	}

	public static UpdateElementAction of(Group element) {
		if(!element.isDynamic()) throw new IllegalArgumentException("Can only update dynamic groups");
		return new UpdateElementAction(element.requireID());
	}

}
