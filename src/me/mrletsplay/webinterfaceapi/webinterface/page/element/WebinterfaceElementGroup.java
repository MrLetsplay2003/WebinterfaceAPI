package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceElementGroup extends AbstractWebinterfacePageElement {

	private List<WebinterfacePageElement> elements;
	
	public WebinterfaceElementGroup() {
		this.elements = new ArrayList<>();
	}
	
	public void addElement(WebinterfacePageElement element) {
		elements.add(element);
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		for(WebinterfacePageElement e : elements) {
			el.appendChild(e.toHtml());
		}
		return el;
	}

}
