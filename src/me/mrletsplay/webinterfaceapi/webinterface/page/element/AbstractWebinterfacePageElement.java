package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public abstract class AbstractWebinterfacePageElement implements WebinterfacePageElement {

	private String id, width, height;
	private WebinterfaceElementLayout layout;
	
	public AbstractWebinterfacePageElement() {
		this.layout = WebinterfaceElementLayout.NONE;
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
	public void setLayout(WebinterfaceElementLayout layout) {
		this.layout = layout;
	}
	
	@Override
	public WebinterfaceElementLayout getLayout() {
		return layout;
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
		String cN = layout.getClassName();
		if(cN != null) elContainer.addClass(cN);
		elContainer.appendChild(el);
		return elContainer;
	}
	
	
}
