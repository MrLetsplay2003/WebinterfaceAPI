package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ArrayValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.RawValue;

public class MultiAction implements WebinterfaceAction {

	private List<WebinterfaceAction> actions;
	
	public MultiAction(List<WebinterfaceAction> actions) {
		this.actions = actions;
	}
	
	public MultiAction(WebinterfaceAction... actions) {
		this(Arrays.asList(actions));
	}
	
	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.multiAction";
	}
	
	@Override
	public ObjectValue getParameters() {
		ObjectValue o = new ObjectValue();
		ArrayValue a = new ArrayValue();
		for(WebinterfaceAction ac : actions) {
			ObjectValue j = new ObjectValue();
			j.put("action", new RawValue(ac.getHandlerName()));
			j.put("parameters", ac.getParameters());
			a.add(j);
		}
		o.put("actions", a);
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		Set<WebinterfaceJSModule> modules = new HashSet<>();
		modules.add(DefaultJSModule.BASE_ACTIONS);
		actions.forEach(a -> modules.addAll(a.getRequiredModules()));
		return modules;
	}
	
}
