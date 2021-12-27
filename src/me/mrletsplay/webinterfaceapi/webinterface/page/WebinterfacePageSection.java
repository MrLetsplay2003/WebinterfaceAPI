package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.css.CssElement;
import me.mrletsplay.webinterfaceapi.css.StyleSheet;
import me.mrletsplay.webinterfaceapi.css.selector.CssSelector;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.page.dynamic.DynamicContent;
import me.mrletsplay.webinterfaceapi.webinterface.page.dynamic.DynamicList;
import me.mrletsplay.webinterfaceapi.webinterface.page.dynamic.DynamicMultiple;
import me.mrletsplay.webinterfaceapi.webinterface.page.dynamic.DynamicOptional;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceHeading;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.ElementLayoutOption;

public class WebinterfacePageSection {
	
	private String id;
	
	private DynamicList<WebinterfacePageElement> elementsList;
	
	private List<ElementLayoutOption>
		layoutOptions;
	
	private CssElement
		style,
		mobileStyle;
	
	private boolean slimLayout;
	
	public WebinterfacePageSection() {
		this.elementsList = new DynamicList<>();
		this.layoutOptions = new ArrayList<>();
		this.style = new CssElement(new CssSelector(() -> "#" + getOrGenerateID()));
		this.mobileStyle = new CssElement(new CssSelector(() -> "#" + getOrGenerateID()));
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}
	
	public String getOrGenerateID() {
		if(getID() == null) setID("s_" + WebinterfaceUtils.randomID(16));
		return getID();
	}
	
	public void addElement(WebinterfacePageElement element) {
		elementsList.addStatic(element);
	}
	
	@Deprecated
	public void addDynamicElements(Supplier<List<WebinterfacePageElement>> elements) {
		elementsList.addDynamicMultiple(list -> list.addAll(elements.get()));
	}
	
	public void dynamic(DynamicContent<WebinterfacePageElement> dynamic) {
		elementsList.addDynamic(dynamic);
	}
	
	public void dynamic(DynamicMultiple<WebinterfacePageElement> dynamic) {
		elementsList.addDynamicMultiple(dynamic);
	}
	
	public void dynamic(DynamicOptional<WebinterfacePageElement> dynamic) {
		elementsList.addDynamicOptional(dynamic);
	}
	
	public void addTitle(Supplier<String> title) {
		WebinterfaceTitleText tt = new WebinterfaceTitleText(title);
		tt.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH, DefaultLayoutOption.CENTER_TEXT);
		addElement(tt);
	}
	
	public void addTitle(String title) {
		addTitle(() -> title);
	}
	
	public void addHeading(Supplier<String> title, int level) {
		WebinterfaceHeading h = new WebinterfaceHeading(title);
		h.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		h.setLevel(level);
		addElement(h);
	}
	
	public void addHeading(String title, int level) {
		addHeading(() -> title, level);
	}
	
	public void addLayoutOptions(ElementLayoutOption... layouts) {
		this.layoutOptions.addAll(Arrays.asList(layouts));
	}
	
	public List<ElementLayoutOption> getLayoutOptions() {
		return layoutOptions;
	}
	
	public CssElement getStyle() {
		return style;
	}
	
	public CssElement getMobileStyle() {
		return mobileStyle;
	}
	
	/**
	 * Sets this page section's layout to be "slim". Enabling this option will reduce the section's maximum width to appear more "slim" when in fullscreen on a Desktop browser.<br>
	 * This should only be used if the containing page only contains this one section
	 * @param slimLayout Whether the slim layout should be used
	 */
	public void setSlimLayout(boolean slimLayout) {
		this.slimLayout = slimLayout;
	}
	
	/**
	 * Returns whether this section uses the "slim" layout
	 * @return Whether this section uses the "slim" layout
	 * @see #setSlimLayout(boolean)
	 */
	public boolean isSlimLayout() {
		return slimLayout;
	}
	
	public HtmlElement toHtml() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout page-section");
		
		if(slimLayout) {
			el.addClass("page-section-slim");
		}
		
		for(WebinterfacePageElement e : elementsList.create()) {
			el.appendChild(e.toHtml());
		}
		
		StyleSheet st = (StyleSheet) ctx.getProperty(WebinterfacePage.CONTEXT_PROPERTY_STYLE);
		if(!style.isEmpty()) {
			el.setID(getOrGenerateID()); // Set id to safe non-null, because it might have not been set before
			st.addElement(style);
		}
		
		if(!mobileStyle.isEmpty()) {
			if(id == null) id = "s_" + WebinterfaceUtils.randomID(16);
			el.setID(getOrGenerateID()); // Set id to safe non-null, because it might have not been set before
			st.addMobileElement(mobileStyle);
		}
		
		layoutOptions.forEach(p -> p.apply(null, el));
		return el;
	}

}
