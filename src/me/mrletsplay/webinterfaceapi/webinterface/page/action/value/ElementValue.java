package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

import org.apache.commons.text.StringEscapeUtils;

import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;

public class ElementValue implements WebinterfaceActionValue {

	private String elementID;
	
	public ElementValue(String elementID) {
		this.elementID = elementID;
	}
	
	public ElementValue(WebinterfacePageElement element) {
		this(element.getOrGenerateID());
	}
	
	@Override
	public String toJavaScript() {
		return "document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(elementID) + "\").value";
	}

}
