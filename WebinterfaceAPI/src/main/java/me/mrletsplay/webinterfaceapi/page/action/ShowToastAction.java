package me.mrletsplay.webinterfaceapi.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;

public class ShowToastAction implements Action {

	private ActionValue message;
	private boolean error;

	private ShowToastAction(ActionValue message, boolean error) {
		this.message = message;
		this.error = error;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.showToast";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue v = ActionValue.object();
		v.put("message", message);
		v.put("error", () -> String.valueOf(error));
		return v;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.TOAST);
	}

	public static ShowToastAction info(ActionValue message) {
		return new ShowToastAction(message, false);
	}

	public static ShowToastAction info(String message) {
		return new ShowToastAction(ActionValue.string(message), false);
	}

	public static ShowToastAction error(ActionValue message) {
		return new ShowToastAction(message, true);
	}

	public static ShowToastAction error(String message) {
		return new ShowToastAction(ActionValue.string(message), true);
	}

}
