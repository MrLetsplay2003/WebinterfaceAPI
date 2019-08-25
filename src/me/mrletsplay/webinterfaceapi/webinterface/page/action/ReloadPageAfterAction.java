package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;

public class ReloadPageAfterAction implements WebinterfaceAction {
	
	private int timeout;
	
	public ReloadPageAfterAction(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public JavaScriptFunction toJavaScript() {
		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
		f.setCode("setTimeout(() => window.location.reload(), " + timeout + ");");
		return f;
	}

}
