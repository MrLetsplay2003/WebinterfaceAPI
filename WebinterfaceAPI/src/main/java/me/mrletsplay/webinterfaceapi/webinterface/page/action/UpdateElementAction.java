package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.JSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Group;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.UpdateableElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.list.WebinterfaceElementList;

public class UpdateElementAction implements Action {

	private Supplier<String> elementID;

	private UpdateElementAction(Supplier<String> elementID) {
		this.elementID = elementID;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.updateElement";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = ActionValue.object();
		o.put("element", ActionValue.string(elementID));
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static UpdateElementAction of(String elementID) {
		return new UpdateElementAction(() -> elementID);
	}

	public static UpdateElementAction of(UpdateableElement element) {
		return new UpdateElementAction(() -> element.getOrGenerateID());
	}

	public static UpdateElementAction of(WebinterfaceElementList<?> element) {
		if(!element.isDynamic()) throw new IllegalArgumentException("Can only update dynamic lists");
		return new UpdateElementAction(() -> element.getOrGenerateID());
	}

	public static UpdateElementAction of(Group element) {
		if(!element.isDynamic()) throw new IllegalArgumentException("Can only update dynamic groups");
		return new UpdateElementAction(() -> element.getOrGenerateID());
	}

}
