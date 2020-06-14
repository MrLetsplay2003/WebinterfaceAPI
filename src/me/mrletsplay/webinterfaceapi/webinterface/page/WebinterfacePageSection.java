package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceHeading;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.ElementLayoutProperty;

public class WebinterfacePageSection {
	
	private Supplier<List<WebinterfacePageElement>> elements;
	
	private List<ElementLayoutProperty>
		innerLayoutProperties;
	
	public WebinterfacePageSection() {
		this.elements = () -> new ArrayList<>();
		this.innerLayoutProperties = new ArrayList<>();
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
		tt.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH, DefaultLayoutProperty.CENTER_VERTICALLY, DefaultLayoutProperty.CENTER_TEXT);
		addElement(tt);
	}
	
	public void addTitle(String title) {
		addTitle(() -> title);
	}
	
	public void addHeading(Supplier<String> title, int level) {
		WebinterfaceHeading h = new WebinterfaceHeading(title);
		h.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH, DefaultLayoutProperty.CENTER_VERTICALLY);
		h.setLevel(level);
		addElement(h);
	}
	
	public void addHeading(String title, int level) {
		addHeading(() -> title, level);
	}
	
	public void addInnerLayoutProperties(ElementLayoutProperty... layouts) {
		this.innerLayoutProperties.addAll(Arrays.asList(layouts));
	}
	
	public List<ElementLayoutProperty> getInnerLayoutProperties() {
		return innerLayoutProperties;
	}
	
	public HtmlElement toHtml() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout page-section");
		for(WebinterfacePageElement e : elements.get()) {
			el.appendChild(e.toHtml());
		}
		innerLayoutProperties.forEach(p -> p.apply(el));
		return el;
	}

}
