package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.Collection;

import me.mrletsplay.simplehttpserver.dom.css.CssElement;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.Action;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.ElementLayoutOption;

public interface PageElement {

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

	public void addLayoutOptions(ElementLayoutOption... layouts);

	public Collection<? extends ElementLayoutOption> getLayoutOptions();

	public void setOnClickAction(Action onClickAction);

	public Action getOnClickAction();

	public void setAttribute(String key, String value);

	public String getAttribute(String key);

	public void setContainerAttribute(String key, String value);

	public String getContainerAttribute(String key);

	public CssElement getStyle();

	public CssElement getMobileStyle();

	/**
	 * Sets whether this element should be treated as a template element.<br>
	 * Template elements can't be added to pages normally, but can instead be used to implement dynamic content using client-side JavaScript.
	 * @param isTemplate Whether this element is a template element
	 */
	public void setTemplate(boolean isTemplate);

	/**
	 * Returns whether this element should be treated as a template element
	 * @return Whether this element should be treated as a template element
	 * @see #setTemplate(boolean)
	 */
	public boolean isTemplate();

	public HtmlElement toHtml();

}