package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Set;

import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;

public interface WebinterfaceAction {
	
	public String getHandlerName();
	
	public ObjectValue getParameters();
	
	public Set<WebinterfaceJSModule> getRequiredModules();
	
	@SuppressWarnings("unchecked")
	public default String createAttributeValue() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		JavaScriptScript sc = (JavaScriptScript) ctx.getProperty(WebinterfacePage.CONTEXT_PROPERTY_SCRIPT);
		String functionName = randomFunctionName();
		JavaScriptFunction f = new JavaScriptFunction(functionName + "()");
		f.addModifier("async");
		f.setCode(String.format("%s(%s);", getHandlerName(), getParameters().toJavaScript()));
		sc.addFunction(f);
		
		Set<WebinterfaceJSModule> m = (Set<WebinterfaceJSModule>) ctx.getProperty(WebinterfacePage.CONTEXT_PROPERTY_REQUIRED_MODULES);
		m.addAll(getRequiredModules());
		
		return String.format("%s()", functionName);
	}
	
	public static String randomFunctionName() {
		return "f_" + WebinterfaceUtils.randomID(16);
	}
	
}
