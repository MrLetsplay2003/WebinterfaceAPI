package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public interface WebinterfacePageElement {
	
	public void setWidth(String width);
	
	public String getWidth();
	
	public void setLayout(WebinterfaceElementLayout layout);
	
	public WebinterfaceElementLayout getLayout();
	
	public HtmlElement toHtml();
	
}
