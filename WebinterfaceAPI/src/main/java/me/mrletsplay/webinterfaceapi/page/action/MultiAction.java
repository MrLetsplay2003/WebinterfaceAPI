package me.mrletsplay.webinterfaceapi.page.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ArrayValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;

public class MultiAction implements Action {

	private List<Action> actions;

	private MultiAction(List<Action> actions) {
		this.actions = actions;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.multiAction";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = ActionValue.object();
		ArrayValue a = ActionValue.array();
		for(Action ac : actions) {
			ObjectValue j = ActionValue.object();
			j.put("action", () -> ac.getHandlerName());
			j.put("parameters", ac.getParameters());
			a.add(j);
		}
		o.put("actions", a);
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		Set<JSModule> modules = new HashSet<>();
		modules.add(DefaultJSModule.BASE_ACTIONS);
		actions.forEach(a -> modules.addAll(a.getRequiredModules()));
		return modules;
	}

	public static MultiAction of(Collection<Action> actions) {
		return new MultiAction(new ArrayList<>(actions));
	}

	public static MultiAction of(Action... actions) {
		return new MultiAction(Arrays.asList(actions));
	}

}
