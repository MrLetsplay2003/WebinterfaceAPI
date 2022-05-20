package me.mrletsplay.webinterfaceapi.page.element;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;

public class RawHtml extends AbstractPageElement {

	private HtmlElement element;

	public RawHtml(HtmlElement element) {
		this.element = element;
	}

	@Override
	public HtmlElement createElement() {
		return element;
	}

}
