package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.JSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.PageElement;

public class SetValueAction implements Action {

	private Supplier<String> elementID;
	private ActionValue value;

	private SetValueAction(Supplier<String> elementID, ActionValue value) {
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
		o.put("element", ActionValue.string(elementID));
		o.put("value", value);
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static SetValueAction of(String elementID, ActionValue value) {
		return new SetValueAction(() -> elementID, value);
	}

	public static SetValueAction of(PageElement element, ActionValue value) {
		return new SetValueAction(() -> element.getOrGenerateID(), value);
	}

}
