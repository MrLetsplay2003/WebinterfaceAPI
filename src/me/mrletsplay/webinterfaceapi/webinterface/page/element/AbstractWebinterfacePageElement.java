package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.ElementLayoutProperty;

public abstract class AbstractWebinterfacePageElement implements WebinterfacePageElement {

	private String
		id,
		width,
		height;
	
	private List<ElementLayoutProperty>
		layoutProperties,
		innerLayoutProperties;
	
	private WebinterfaceAction onClickAction;
	
	private Map<String, String>
		attributes,
		containerAttributes;
	
	public AbstractWebinterfacePageElement() {
		this.layoutProperties = new ArrayList<>();
		this.innerLayoutProperties = new ArrayList<>();
		this.attributes = new HashMap<>();
		this.containerAttributes = new HashMap<>();
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
	public void addLayoutProperties(ElementLayoutProperty... layouts) {
		this.layoutProperties.addAll(Arrays.asList(layouts));
	}
	
	@Override
	public List<ElementLayoutProperty> getLayoutProperties() {
		return layoutProperties;
	}
	
	@Override
	public void addInnerLayoutProperties(ElementLayoutProperty... layouts) {
		this.innerLayoutProperties.addAll(Arrays.asList(layouts));
	}
	
	@Override
	public List<ElementLayoutProperty> getInnerLayoutProperties() {
		return innerLayoutProperties;
	}
	
	@Override
	public void setOnClickAction(WebinterfaceAction onClickAction) {
		this.onClickAction = onClickAction;
	}
	
	@Override
	public WebinterfaceAction getOnClickAction() {
		return onClickAction;
	}
	
	@Override
	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}
	
	@Override
	public String getAttribute(String key) {
		return attributes.get(key);
	}
	
	@Override
	public void setContainerAttribute(String key, String value) {
		containerAttributes.put(key, value);
	}
	
	@Override
	public String getContainerAttribute(String key) {
		return containerAttributes.get(key);
	}
	
	public abstract HtmlElement createElement();
	
	@Override
	public HtmlElement toHtml() {
		HtmlElement elContainer = new HtmlElement("div");
		elContainer.addClass("element-container");
		HtmlElement el = createElement();
		if(id != null && el.getID() == null) el.setID(id);
		if(width != null) el.setAttribute("style", "width:" + width + "");
		el.addClass("element");
		if(onClickAction != null) {
			JavaScriptScript sc = (JavaScriptScript) HttpRequestContext.getCurrentContext().getProperty(WebinterfacePage.CONTEXT_PROPERTY_SCRIPT);
			JavaScriptFunction f = onClickAction.toJavaScript();
			sc.addFunction(f);
			elContainer.setAttribute("onclick", f.getSignature());
		}
		attributes.forEach(el::setAttribute);
		containerAttributes.forEach(elContainer::setAttribute);
		layoutProperties.forEach(p -> p.apply(elContainer));
		innerLayoutProperties.forEach(p -> p.apply(el));
		elContainer.appendChild(el);
		return elContainer;
	}
	
	
}
