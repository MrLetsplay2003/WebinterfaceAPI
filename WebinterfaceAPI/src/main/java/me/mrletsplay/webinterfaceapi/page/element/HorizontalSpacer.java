package me.mrletsplay.webinterfaceapi.page.element;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;

public class HorizontalSpacer extends AbstractPageElement {

	private String width;

	public HorizontalSpacer(String width) {
		this.width = width;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.setAttribute("style", "width: " + width);
		return el;
	}

}
