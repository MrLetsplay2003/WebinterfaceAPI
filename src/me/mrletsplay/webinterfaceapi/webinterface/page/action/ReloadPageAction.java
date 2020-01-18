package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;

public class ReloadPageAction implements WebinterfaceAction {
	
	private boolean forceGet;
	
	public ReloadPageAction(boolean forceGet) {
		this.forceGet = forceGet;
	}
	
	public ReloadPageAction() {
		this(false);
	}

	@Override
	public JavaScriptFunction toJavaScript() {
		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
		f.setCode("window.location.reload(" + forceGet + ");");
		return f;
	}

}
