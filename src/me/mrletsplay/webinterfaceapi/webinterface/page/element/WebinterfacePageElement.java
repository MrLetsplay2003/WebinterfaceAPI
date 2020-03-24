package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.List;

import me.mrletsplay.webinterfaceapi.css.CssElement;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.ElementLayoutProperty;

public interface WebinterfacePageElement {
	
	public void setID(String id);
	
	public String getID();
	
	public default String getOrGenerateID() {
		if(getID() == null) setID("e_" + WebinterfaceUtils.randomID(16));
		return getID();
	}
	
	public void setWidth(String width);
	
	public String getWidth();
	
	public void setHeight(String height);
	
	public String getHeight();
	
	public void addLayoutProperties(ElementLayoutProperty... layouts);
	
	public List<ElementLayoutProperty> getLayoutProperties();
	
	public void addInnerLayoutProperties(ElementLayoutProperty... layouts);
	
	public List<ElementLayoutProperty> getInnerLayoutProperties();
	
	public void setOnClickAction(WebinterfaceAction onClickAction);
	
	public WebinterfaceAction getOnClickAction();
	
	public void setAttribute(String key, String value);
	
	public String getAttribute(String key);
	
	public void setContainerAttribute(String key, String value);
	
	public String getContainerAttribute(String key);
	
	public CssElement getStyle();
	
	public CssElement getMobileStyle();
	
	public HtmlElement toHtml();
	
}
