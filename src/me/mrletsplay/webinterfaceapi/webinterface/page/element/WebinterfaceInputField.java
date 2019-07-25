package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceInputField extends AbstractWebinterfacePageElement {
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("input");
		b.setAttribute("type", "text");
		b.setAttribute("placeholder", "Text");
		return b;
	}

}
