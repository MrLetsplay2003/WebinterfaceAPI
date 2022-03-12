package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.RawValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceFileUpload;

public class SubmitUploadAction implements WebinterfaceAction {

	private String elementID;
	private WebinterfaceAction onSucess;
	private WebinterfaceAction onError;
	
	public SubmitUploadAction(String elementID) {
		this.elementID = elementID;
	}
	
	public SubmitUploadAction(WebinterfaceFileUpload element) {
		this(element.getOrGenerateID());
	}
	
	public SubmitUploadAction onSuccess(WebinterfaceAction onSuccess) {
		this.onSucess = onSuccess;
		return this;
	}
	
	public SubmitUploadAction onError(WebinterfaceAction onError) {
		this.onError = onError;
		return this;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.submitUpload";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = new ObjectValue();
		o.put("element", new StringValue(elementID));
		
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
		
		return o;
	}

	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
