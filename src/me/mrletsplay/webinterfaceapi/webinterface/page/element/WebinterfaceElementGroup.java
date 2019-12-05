package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;

public class WebinterfaceElementGroup extends AbstractWebinterfacePageElement {

	private List<WebinterfacePageElement> elements;
	
	public WebinterfaceElementGroup() {
		this.elements = new ArrayList<>();
	}
	
	public void addTitle(Supplier<String> title) {
		WebinterfaceTitleText t = new WebinterfaceTitleText(title);
		t.setText(title);
		t.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
		addElement(t);
	}
	
	public void addTitle(String title) {
		addTitle(() -> title);
	}
	
	public void addElement(WebinterfacePageElement element) {
		elements.add(element);
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout");
		for(WebinterfacePageElement e : elements) {
			el.appendChild(e.toHtml());
		}
		return el;
	}

}
