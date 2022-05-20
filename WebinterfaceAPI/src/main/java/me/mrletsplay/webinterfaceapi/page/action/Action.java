package me.mrletsplay.webinterfaceapi.page.action;

import java.util.Set;

import me.mrletsplay.webinterfaceapi.context.WebinterfaceContext;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public interface Action {

	public String getHandlerName();

	public ActionValue getParameters();

	public Set<JSModule> getRequiredModules();

	public default String createAttributeValue() {
		WebinterfaceContext ctx = WebinterfaceContext.getCurrentContext();
		ctx.requireModules(getRequiredModules());
		return getCode();
	}

	public default String getCode() {
		return String.format("%s(%s);", getHandlerName(), getParameters().toJavaScript());
	}

	public static String randomFunctionName() {
		return "f_" + WebinterfaceUtils.randomID(16);
	}

}
