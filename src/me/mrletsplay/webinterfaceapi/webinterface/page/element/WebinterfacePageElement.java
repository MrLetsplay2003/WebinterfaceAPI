package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public interface WebinterfacePageElement {
	
	public void setLayout(WebinterfaceElementLayout layout);
	
	public WebinterfaceElementLayout getLayout();
	
	public HtmlElement toHtml();
	
}
