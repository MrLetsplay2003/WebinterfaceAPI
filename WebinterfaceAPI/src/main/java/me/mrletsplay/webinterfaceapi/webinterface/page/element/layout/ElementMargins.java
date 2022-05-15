package me.mrletsplay.webinterfaceapi.webinterface.page.element.layout;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;

public class ElementMargins implements ElementLayoutOption {

	private String marginLeft;
	private String marginRight;
	private String marginTop;
	private String marginBottom;

	private ElementMargins(String marginLeft, String marginRight, String marginTop, String marginBottom) {
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;
	}

	public ElementMargins(String margin) {
		this(margin, margin, margin, margin);
	}

	@Override
	public void apply(HtmlElement elementContainer, HtmlElement element) {
		element.appendAttribute("style", String.format("margin: %s %s %s %s;", marginTop, marginRight, marginBottom, marginLeft));
	}

}
