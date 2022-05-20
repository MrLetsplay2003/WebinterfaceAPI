package me.mrletsplay.webinterfaceapi.page.action;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.page.element.FileUpload;

public class SubmitUploadAction implements Action {

	private Supplier<String> elementID;
	private Action onSucess;
	private Action onError;

	private SubmitUploadAction(Supplier<String> elementID) {
		this.elementID = elementID;
	}

	public SubmitUploadAction onSuccess(Action onSuccess) {
		this.onSucess = onSuccess;
		return this;
	}

	public SubmitUploadAction onError(Action onError) {
		this.onError = onError;
		return this;
	}

	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.submitUpload";
	}

	@Override
	public ObjectValue getParameters() {
		ObjectValue o = ActionValue.object();
		o.put("element", ActionValue.string(elementID));

		if(onSucess != null) {
			ObjectValue j = ActionValue.object();
			j.put("action", () -> onSucess.getHandlerName());
			j.put("parameters", onSucess.getParameters());
			o.put("onSuccess", j);
		}

		if(onError != null) {
			ObjectValue j = ActionValue.object();
			j.put("action", () -> onError.getHandlerName());
			j.put("parameters", onError.getParameters());
			o.put("onError", j);
		}

		return o;
	}

	@Override
	public Set<JSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

	public static SubmitUploadAction of(String elementID) {
		return new SubmitUploadAction(() -> elementID);
	}

	public static SubmitUploadAction of(FileUpload element) {
		return new SubmitUploadAction(() -> element.getOrGenerateID());
	}

}
