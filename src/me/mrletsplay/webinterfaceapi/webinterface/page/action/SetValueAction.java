package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;

public class SetValueAction implements WebinterfaceAction {

	private String elementID;
	private WebinterfaceActionValue value;
	
	public SetValueAction(String elementID, WebinterfaceActionValue value) {
		this.elementID = elementID;
		this.value = value;
	}
	
	public SetValueAction(WebinterfacePageElement element, WebinterfaceActionValue value) {
		this(element.getOrGenerateID(), value);
	}

//	public JavaScriptFunction toJavaScript() {
//		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
//		f.setCode("document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(elementID) + "\").value=" + value.toJavaScript() + ";");
//		return f;
//	}
	
	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.setValue";
	}
	
	public JSONObject getParameters() {
		JSONObject o = new JSONObject();
		o.put("element", elementID);
		o.put("value", value.toJavaScript());
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
