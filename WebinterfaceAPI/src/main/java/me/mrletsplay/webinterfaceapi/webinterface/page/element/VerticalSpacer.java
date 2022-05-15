package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;

public class VerticalSpacer extends AbstractPageElement {

	private String height;

	public VerticalSpacer(String height) {
		this.height = height;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.setAttribute("style", "height: " + height);
		return el;
	}

}
