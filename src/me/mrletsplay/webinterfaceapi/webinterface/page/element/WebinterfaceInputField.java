package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceInputField extends AbstractWebinterfacePageElement {
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = HtmlElement.button();
		b.setText("I'm a button");
		return b;
	}

}
