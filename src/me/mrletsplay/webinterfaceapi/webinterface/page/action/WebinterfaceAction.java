package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public interface WebinterfaceAction {
	
	public JavaScriptFunction toJavaScript();
	
	public default String randomFunctionName() {
		return "f_" + WebinterfaceUtils.randomID(16);
	}
	
}
