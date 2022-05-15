package me.mrletsplay.webinterfaceapi.webinterface.page.element.layout;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;

public interface ElementLayoutOption {

	public void apply(HtmlElement elementContainer, HtmlElement element) throws UnsupportedOperationException;

}
