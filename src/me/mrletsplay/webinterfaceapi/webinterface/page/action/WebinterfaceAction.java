package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;

public interface WebinterfaceAction {
	
	public JavaScriptFunction toJavaScript();
	
	public default String randomFunctionName() {
		return "f_" + System.currentTimeMillis() + (int) (100*Math.random());
	}
	
}
