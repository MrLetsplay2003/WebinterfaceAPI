package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceHorizontalSpacer extends AbstractWebinterfacePageElement {

	private String width;
	
	public WebinterfaceHorizontalSpacer(String width) {
		this.width = width;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.setAttribute("style", "width: " + width);
		return el;
	}

}
