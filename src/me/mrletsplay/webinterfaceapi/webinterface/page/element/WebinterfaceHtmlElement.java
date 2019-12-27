package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceHtmlElement extends AbstractWebinterfacePageElement {

	private HtmlElement element;
	
	public WebinterfaceHtmlElement(HtmlElement element) {
		this.element = element;
	}
	
	@Override
	public HtmlElement createElement() {
		return element;
	}

}
