package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Collections;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
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
	public ObjectValue getParameters() {
		ObjectValue o = new ObjectValue();
		o.put("requestTarget", new StringValue(target));
		o.put("requestMethod", new StringValue(method));
		o.put("value", value);
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}

}
