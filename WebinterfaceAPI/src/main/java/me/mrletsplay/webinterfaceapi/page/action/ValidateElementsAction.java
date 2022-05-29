package me.mrletsplay.webinterfaceapi.page.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.page.element.ElementID;
import me.mrletsplay.webinterfaceapi.page.element.PageElement;

public class ValidateElementsAction implements Action {

	private List<ElementID> elements;

	private Action onSuccess;
	private Action onError;

	public ValidateElementsAction(List<ElementID> elements) {
		this.elements = elements;
		elements.forEach(e -> e.require());
	}

	public ValidateElementsAction onSuccess(Action onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	public ValidateElementsAction onError(Action onError) {
		this.onError = onError;
		return this;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.validateElements";
	}

	@Override
	public ActionValue getParameters() {
		ObjectValue v = ActionValue.object();
		v.put("elements", ActionValue.array(elements.stream().map(e -> ActionValue.string(e.get())).collect(Collectors.toList())));

		if(onSuccess != null) {
			ObjectValue j = ActionValue.object();
			j.put("action", () -> onSuccess.getHandlerName());
			j.put("parameters", onSuccess.getParameters());
			v.put("onSuccess", j);
		}

		if(onError != null) {
			ObjectValue j = ActionValue.object();
			j.put("action", () -> onError.getHandlerName());
			j.put("parameters", onError.getParameters());
			v.put("onError", j);
		}

		return v;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static ValidateElementsAction ofIDs(Collection<ElementID> elementIDs) {
		return new ValidateElementsAction(new ArrayList<>(elementIDs));
	}

	public static ValidateElementsAction of(Collection<? extends PageElement> elements) {
		return ofIDs(elements.stream()
			.map(e -> e.getID())
			.collect(Collectors.toList()));
	}

	public static ValidateElementsAction ofIDs(ElementID... elementIDs) {
		return ofIDs(Arrays.asList(elementIDs));
	}

	public static ValidateElementsAction of(PageElement... elements) {
		return of(Arrays.asList(elements));
	}

}
