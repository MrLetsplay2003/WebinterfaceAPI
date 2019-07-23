package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;

public class WebinterfacePageSection {
	
	private List<WebinterfacePageElement> elements;
	
	public WebinterfacePageSection() {
		this.elements = new ArrayList<>();
	}
	
	public void addElement(WebinterfacePageElement element) {
		elements.add(element);
	}
	
	public HtmlElement toHtml() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout page-section");
		for(WebinterfacePageElement e : elements) {
			el.appendChild(e.toHtml());
		}
		return el;
	}

}
