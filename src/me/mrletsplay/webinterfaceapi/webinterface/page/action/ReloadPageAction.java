package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;

public class ReloadPageAction implements WebinterfaceAction {

	@Override
	public JavaScriptFunction toJavaScript() {
		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
		f.setCode("window.location.reload();");
		return f;
	}

}
