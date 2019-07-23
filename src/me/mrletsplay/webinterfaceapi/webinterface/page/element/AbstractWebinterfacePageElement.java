package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public abstract class AbstractWebinterfacePageElement implements WebinterfacePageElement {

	private WebinterfaceElementLayout layout;
	
	public AbstractWebinterfacePageElement() {
		this.layout = WebinterfaceElementLayout.NONE;
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
		HtmlElement el = createElement();
		el.setAttribute("class", layout.getClassName());
		return el;
	}
	
	
}
