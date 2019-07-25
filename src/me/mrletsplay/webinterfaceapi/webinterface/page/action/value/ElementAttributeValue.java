package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

import org.apache.commons.text.StringEscapeUtils;

public class ElementAttributeValue implements WebinterfaceActionValue {

	private String elementID, attributeName;
	
	public ElementAttributeValue(String elementID, String attributeName) {
		this.elementID = elementID;
		this.attributeName = attributeName;
	}
	
	@Override
	public String toJavaScript() {
		return "WebinterfaceUtils.getElementAttributeById(\"" + StringEscapeUtils.escapeEcmaScript(elementID) + "\",\"" + StringEscapeUtils.escapeEcmaScript(attributeName) + "\")";
	}

}
