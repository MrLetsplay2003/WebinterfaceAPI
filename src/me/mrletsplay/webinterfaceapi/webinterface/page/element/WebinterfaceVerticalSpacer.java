package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceVerticalSpacer extends AbstractWebinterfacePageElement {

	private String height;
	
	public WebinterfaceVerticalSpacer(String height) {
		this.height = height;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.setAttribute("style", "height: " + height);
		return el;
	}

}
