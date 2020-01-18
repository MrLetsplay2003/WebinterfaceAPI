package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;

public class ReloadPageAfterAction implements WebinterfaceAction {
	
	private int timeout;
	private boolean forceGet;
	
	public ReloadPageAfterAction(int timeout, boolean forceGet) {
		this.timeout = timeout;
		this.forceGet = forceGet;
	}
	
	public ReloadPageAfterAction(int timeout) {
		this(timeout, false);
	}

	@Override
	public JavaScriptFunction toJavaScript() {
		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
		f.setCode("setTimeout(() => window.location.reload(" + forceGet + "), " + timeout + ");");
		return f;
	}

}
