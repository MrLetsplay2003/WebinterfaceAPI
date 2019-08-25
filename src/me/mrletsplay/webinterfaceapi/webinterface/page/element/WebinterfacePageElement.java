package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.List;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public interface WebinterfacePageElement {
	
	public void setID(String id);
	
	public String getID();
	
	public default String getOrGenerateID() {
		if(getID() == null) setID(Long.toHexString(System.currentTimeMillis()) + Integer.toHexString((int) (256*Math.random()))); // TODO: better ids?
		return getID();
	}
	
	public void setWidth(String width);
	
	public String getWidth();
	
	public void setHeight(String height);
	
	public String getHeight();
	
	public void addLayouts(ElementLayout... layouts);
	
	public List<ElementLayout> getLayouts();
	
	public HtmlElement toHtml();
	
}
