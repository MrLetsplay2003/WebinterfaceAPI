package me.mrletsplay.webinterfaceapi.page.element;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.mrletsplay.simplehttpserver.dom.css.CssElement;
import me.mrletsplay.simplehttpserver.dom.css.CssSelector;
import me.mrletsplay.simplehttpserver.dom.css.StyleSheet;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.element.layout.ElementLayoutOption;

public abstract class AbstractPageElement implements PageElement {

	private String
		id,
		width,
		height;

	private Set<ElementLayoutOption> layoutOptions;

	private Action onClickAction;

	private Map<String, String>
		attributes,
		containerAttributes;

	private CssElement
		style,
		mobileStyle;

	private boolean isTemplate;

	public AbstractPageElement() {
		this.layoutOptions = new HashSet<>();
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
	public void addLayoutOptions(ElementLayoutOption... layouts) {
		this.layoutOptions.addAll(Arrays.asList(layouts));
	}

	@Override
	public Collection<? extends ElementLayoutOption> getLayoutOptions() {
		return layoutOptions;
	}

	@Override
	public void setOnClickAction(Action onClickAction) {
		this.onClickAction = onClickAction;
	}

	@Override
	public Action getOnClickAction() {
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

	@Override
	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	@Override
	public boolean isTemplate() {
		return isTemplate;
	}

	public abstract HtmlElement createElement();

	@Override
	public HtmlElement toHtml() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();

		HtmlElement elContainer = new HtmlElement("div");
		elContainer.addClass("element-container");
		HtmlElement el = createElement();
		if(id != null && el.getID() == null) el.setID(id);
		if(width != null) style.setProperty("width", width);
		if(height != null) style.setProperty("height", height);

		el.addClass("element");
		if(onClickAction != null) el.setAttribute("onclick", onClickAction.createAttributeValue());

		StyleSheet st = (StyleSheet) ctx.getProperty(Page.CONTEXT_PROPERTY_STYLE);
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
		layoutOptions.forEach(p -> p.apply(elContainer, el));
		elContainer.appendChild(el);
		return elContainer;
	}


}
