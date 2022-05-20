package me.mrletsplay.webinterfaceapi.page.action;

import java.util.Set;

import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public interface Action {

	public String getHandlerName();

	public ObjectValue getParameters();

	public Set<JSModule> getRequiredModules();

	@SuppressWarnings("unchecked")
	public default String createAttributeValue() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		Set<JSModule> m = (Set<JSModule>) ctx.getProperty(Page.CONTEXT_PROPERTY_REQUIRED_MODULES);
		m.addAll(getRequiredModules());
		return getCode();
	}

	public default String getCode() {
		return String.format("%s(%s);", getHandlerName(), getParameters().toJavaScript());
	}

	public static String randomFunctionName() {
		return "f_" + WebinterfaceUtils.randomID(16);
	}

}
