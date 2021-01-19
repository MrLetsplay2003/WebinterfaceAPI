package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;

public class SendJSAction implements WebinterfaceAction {

	private String
		target,
		method;
	
	private WebinterfaceActionValue value;
	
	public SendJSAction(String target, String method, WebinterfaceActionValue js) {
		this.target = target;
		this.method = method;
		this.value = js;
	}
	
//	@Override
//	public JavaScriptFunction toJavaScript() {
//		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
//		f.setCode("Webinterface.call(\"" + StringEscapeUtils.escapeEcmaScript(target) + "\",\""
//				+ StringEscapeUtils.escapeEcmaScript(method) + "\",{\"value\":"
//				+ (value == null ? "null" : value.toJavaScript()) + "});");
//		return f;
//	}
	
	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.sendJS";
	}
	
	@Override
	public JSONObject getParameters() {
		JSONObject o = new JSONObject();
		o.put("requestTarget", target);
		o.put("requestMethod", method);
		o.put("value", value == null ? null : value.toJavaScript());
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
