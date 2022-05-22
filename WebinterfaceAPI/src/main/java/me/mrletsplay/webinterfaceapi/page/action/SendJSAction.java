package me.mrletsplay.webinterfaceapi.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;

public class SendJSAction implements Action {

	private String
		target,
		method;

	private ActionValue value;
	private Action onSuccess;
	private Action onError;

	private SendJSAction(String target, String method, ObjectValue value) {
		this.target = target;
		this.method = method;
		this.value = value;
	}

	public SendJSAction onSuccess(Action onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	public SendJSAction onError(Action onError) {
		this.onError = onError;
		return this;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.sendJS";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = ActionValue.object();
		o.put("requestTarget", ActionValue.string(target));
		o.put("requestMethod", ActionValue.string(method));

		if(onSuccess != null) {
			ObjectValue j = ActionValue.object();
			j.put("action", () -> onSuccess.getHandlerName());
			j.put("parameters", onSuccess.getParameters());
			o.put("onSuccess", j);
		}

		if(onError != null) {
			ObjectValue j = ActionValue.object();
			j.put("action", () -> onError.getHandlerName());
			j.put("parameters", onError.getParameters());
			o.put("onError", j);
		}

		o.put("value", value);
		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static SendJSAction of(String target, String method, ObjectValue value) {
		return new SendJSAction(target, method, value);
	}

}
