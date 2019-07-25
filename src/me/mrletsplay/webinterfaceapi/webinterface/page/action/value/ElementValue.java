package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

import org.apache.commons.text.StringEscapeUtils;

public class ElementValue implements WebinterfaceActionValue {

	private String elementID;
	
	public ElementValue(String elementID) {
		this.elementID = elementID;
	}
	
	@Override
	public String toJavaScript() {
		return "document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(elementID) + "\").value";
	}

}
