package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.RawValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;

public class SendJSAction implements WebinterfaceAction {

	private String
		target,
		method;
	
	private WebinterfaceActionValue value;
	private WebinterfaceAction onSucess;
	private WebinterfaceAction onError;
	
	public SendJSAction(String target, String method, WebinterfaceActionValue value) {
		this.target = target;
		this.method = method;
		this.value = value;
	}
	
	public SendJSAction onSuccess(WebinterfaceAction onSuccess) {
		this.onSucess = onSuccess;
		return this;
	}
	
	public SendJSAction onError(WebinterfaceAction onError) {
		this.onError = onError;
		return this;
	}
	
	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.sendJS";
	}
	
	@Override
	public ObjectValue getParameters() {
		ObjectValue o = new ObjectValue();
		o.put("requestTarget", new StringValue(target));
		o.put("requestMethod", new StringValue(method));
		
		if(onSucess != null) {
			ObjectValue j = new ObjectValue();
			j.put("action", new RawValue(onSucess.getHandlerName()));
			j.put("parameters", onSucess.getParameters());
			o.put("onSuccess", j);
		}
		
		if(onError != null) {
			ObjectValue j = new ObjectValue();
			j.put("action", new RawValue(onError.getHandlerName()));
			j.put("parameters", onError.getParameters());
			o.put("onError", j);
		}
		
		o.put("value", value);
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
