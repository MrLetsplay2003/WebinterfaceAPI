package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;

public class WebinterfacePageSection {
	
	private Supplier<List<WebinterfacePageElement>> elements;
	
	public WebinterfacePageSection() {
		this.elements = () -> new ArrayList<>();
	}
	
	public void addElement(WebinterfacePageElement element) {
		addDynamicElements(() -> Collections.singletonList(element));
	}
	
	public void addDynamicElements(Supplier<List<WebinterfacePageElement>> elements) {
		Supplier<List<WebinterfacePageElement>> oldEls = this.elements;
		this.elements = () -> {
			List<WebinterfacePageElement> ss = new ArrayList<>(oldEls.get());
			ss.addAll(elements.get());
			return ss;
		};
	}
	
	public void addTitle(Supplier<String> title) {
		WebinterfaceTitleText tt = new WebinterfaceTitleText(title);
		tt.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH, DefaultLayoutProperty.CENTER_VERTICALLY);
		addElement(tt);
	}
	
	public void addTitle(String title) {
		addTitle(() -> title);
	}
	
	public HtmlElement toHtml() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout page-section");
		for(WebinterfacePageElement e : elements.get()) {
			el.appendChild(e.toHtml());
		}
		return el;
	}

}
