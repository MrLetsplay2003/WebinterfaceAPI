package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import org.apache.commons.text.StringEscapeUtils;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;

public class SendJSAction implements WebinterfaceAction {

	private String target, method;
	private WebinterfaceActionValue value;
	
	public SendJSAction(String target, String method, WebinterfaceActionValue js) {
		this.target = target;
		this.method = method;
		this.value = js;
	}
	
	@Override
	public JavaScriptFunction toJavaScript() {
		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
		f.setCode("Webinterface.call(\"" + StringEscapeUtils.escapeEcmaScript(target) + "\",\""
				+ StringEscapeUtils.escapeEcmaScript(method) + "\",{\"value\":"
				+ (value == null ? "null" : value.toJavaScript()) + "});");
		return f;
	}

}
