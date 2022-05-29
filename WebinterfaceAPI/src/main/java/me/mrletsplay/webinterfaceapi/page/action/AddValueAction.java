package me.mrletsplay.webinterfaceapi.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.page.element.ElementID;
import me.mrletsplay.webinterfaceapi.page.element.PageElement;

public class AddValueAction implements Action {

	private ElementID elementID;
	private ActionValue value;
	private boolean triggerUpdate;

	private AddValueAction(ElementID elementID, ActionValue value) {
		this.elementID = elementID;
		this.value = value;
		this.triggerUpdate = true;
	}

	public AddValueAction triggerUpdate(boolean triggerUpdate) {
		this.triggerUpdate = triggerUpdate;
		return this;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.addValue";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = ActionValue.object();
		o.put("element", ActionValue.string(elementID.get()));
		o.put("value", value);
		o.put("triggerUpdate", ActionValue.bool(triggerUpdate));
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static AddValueAction of(ElementID elementID, ActionValue value) {
		elementID.require();
		return new AddValueAction(elementID, value);
	}

	public static AddValueAction of(PageElement element, ActionValue value) {
		return new AddValueAction(element.requireID(), value);
	}

}
