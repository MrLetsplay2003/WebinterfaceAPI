package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public abstract class AbstractWebinterfacePageElement implements WebinterfacePageElement {

	private String id, width, height;
	private List<ElementLayout> layouts;
	
	public AbstractWebinterfacePageElement() {
		this.layouts = new ArrayList<>();
	}
	
	@Override
	public void setID(String id) {
		this.id = id;
	}
	
	@Override
	public String getID() {
		return id;
	}
	
	@Override
	public void setWidth(String width) {
		this.width = width;
	}
	
	@Override
	public String getWidth() {
		return width;
	}
	
	@Override
	public void setHeight(String height) {
		this.height = height;
	}
	
	@Override
	public String getHeight() {
		return height;
	}
	
	@Override
	public void addLayouts(ElementLayout... layouts) {
		this.layouts.addAll(Arrays.asList(layouts));
	}
	
	@Override
	public List<ElementLayout> getLayouts() {
		return layouts;
	}
	
	public abstract HtmlElement createElement();
	
	@Override
	public HtmlElement toHtml() {
		HtmlElement elContainer = new HtmlElement("div");
		elContainer.addClass("element-container");
		HtmlElement el = createElement();
		if(id != null) el.setID(id);
		if(width != null) el.setAttribute("style", "width:" + width + "");
		el.addClass("element");
		String cN = layouts.stream().map(ElementLayout::getClassName).collect(Collectors.joining(" "));
		if(!cN.isEmpty()) elContainer.addClass(cN);
		elContainer.appendChild(el);
		return elContainer;
	}
	
	
}
