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
import me.mrletsplay.webinterfaceapi.context.WebinterfaceContext;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.element.layout.ElementLayoutOption;

public abstract class AbstractPageElement implements PageElement {

	private ElementID id;

	private String
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
		this.id = new ElementID();
		this.layoutOptions = new HashSet<>();
		this.attributes = new HashMap<>();
		this.containerAttributes = new HashMap<>();
		this.style = new CssElement(new CssSelector(() -> "#" + id.get()));
		this.mobileStyle = new CssElement(new CssSelector(() -> "#" + id.get()));
	}

	@Override
	public void setID(String id) {
		this.id.set(id);
	}

	@Override
	public ElementID getID() {
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
		HtmlElement elContainer = new HtmlElement("div");
		elContainer.addClass("element-container");
		if(width != null) style.setProperty("width", width);
		if(height != null) style.setProperty("height", height);

		WebinterfaceContext ctx = WebinterfaceContext.getCurrentContext();
		StyleSheet st = ctx.getStyle();
		if(!style.isEmpty()) {
			id.require();
			st.addElement(style);
		}

		if(!mobileStyle.isEmpty()) {
			id.require();
			st.addMobileElement(mobileStyle);
		}

		HtmlElement el = createElement();
		el.addClass("element");
		if(id.get() != null) el.setID(id.get());
		if(onClickAction != null) el.setAttribute("onclick", onClickAction.createAttributeValue());

		attributes.forEach(el::setAttribute);
		containerAttributes.forEach(elContainer::setAttribute);
		layoutOptions.forEach(p -> p.apply(elContainer, el));
		elContainer.appendChild(el);
		return elContainer;
	}


}
