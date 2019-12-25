package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import org.apache.commons.text.StringEscapeUtils;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;

public class SetValueAction implements WebinterfaceAction {

	private String elementID;
	private WebinterfaceActionValue value;
	
	public SetValueAction(String elementID, WebinterfaceActionValue value) {
		this.elementID = elementID;
		this.value = value;
	}
	
	public SetValueAction(WebinterfacePageElement element, WebinterfaceActionValue value) {
		this(element.getOrGenerateID(), value);
	}

	@Override
	public JavaScriptFunction toJavaScript() {
		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
		f.setCode("document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(elementID) + "\").value=" + value.toJavaScript() + ";");
		return f;
	}
	
	

}
