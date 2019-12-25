package me.mrletsplay.webinterfaceapi.webinterface.page.element.layout;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class MarginLeft implements ElementLayoutProperty {

	private String marginLeft;
	
	public MarginLeft(String marginLeft) {
		this.marginLeft = marginLeft;
	}
	
	@Override
	public void apply(HtmlElement element) {
		element.appendAttribute("style", "margin-left: " + marginLeft + ";");
	}
	
}
