package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.JSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;

public class ConfirmAction implements Action {

	private Action action;

	private ConfirmAction(Action action) {
		this.action = action;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.confirm";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = ActionValue.object();
		o.put("action", () -> action.getHandlerName());
		o.put("actionParameters", action.getParameters());
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static ConfirmAction of(Action action) {
		return new ConfirmAction(action);
	}

}
