package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mrletsplay.webinterfaceapi.css.CssElement;
import me.mrletsplay.webinterfaceapi.css.StyleSheet;
import me.mrletsplay.webinterfaceapi.css.selector.CssSelector;
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
	
	private CssElement
		style,
		mobileStyle;
	
	public AbstractWebinterfacePageElement() {
		this.layoutProperties = new ArrayList<>();
		this.innerLayoutProperties = new ArrayList<>();
		this.attributes = new HashMap<>();
		this.containerAttributes = new HashMap<>();
		this.style = new CssElement(new CssSelector(() -> "#" + getOrGenerateID()));
		this.mobileStyle = new CssElement(new CssSelector(() -> "#" + getOrGenerateID()));
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
	
	@Override
	public CssElement getStyle() {
		return style;
	}
	
	@Override
	public CssElement getMobileStyle() {
		return mobileStyle;
	}
	
	public abstract HtmlElement createElement();
	
	@Override
	public HtmlElement toHtml() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		
		HtmlElement elContainer = new HtmlElement("div");
		elContainer.addClass("element-container");
		HtmlElement el = createElement();
		if(id != null && el.getID() == null) el.setID(id);
		if(width != null) el.appendAttribute("style", "width:" + width + ";");
		if(height != null) el.appendAttribute("style", "height:" + height + ";");
		el.addClass("element");
		if(onClickAction != null) {
			JavaScriptScript sc = (JavaScriptScript) ctx.getProperty(WebinterfacePage.CONTEXT_PROPERTY_SCRIPT);
			JavaScriptFunction f = onClickAction.toJavaScript();
			sc.addFunction(f);
			el.setAttribute("onclick", f.getSignature());
		}
		
		StyleSheet st = (StyleSheet) ctx.getProperty(WebinterfacePage.CONTEXT_PROPERTY_STYLE);
		if(!style.isEmpty()) {
			el.setID(getOrGenerateID()); // Set id to safe non-null, because it might have not been set before
			st.addElement(style);
		}
		
		if(!mobileStyle.isEmpty()) {
			el.setID(getOrGenerateID()); // Set id to safe non-null, because it might have not been set before
			st.addMobileElement(mobileStyle);
		}
		
		attributes.forEach(el::setAttribute);
		containerAttributes.forEach(elContainer::setAttribute);
		layoutProperties.forEach(p -> p.apply(elContainer));
		innerLayoutProperties.forEach(p -> p.apply(el));
		elContainer.appendChild(el);
		return elContainer;
	}
	
	
}
