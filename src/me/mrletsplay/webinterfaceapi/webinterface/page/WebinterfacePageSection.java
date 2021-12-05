package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.css.CssElement;
import me.mrletsplay.webinterfaceapi.css.StyleSheet;
import me.mrletsplay.webinterfaceapi.css.selector.CssSelector;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceHeading;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.ElementLayoutOption;

public class WebinterfacePageSection {
	
	private String id;
	
	private Supplier<List<WebinterfacePageElement>> elements;
	
	private List<ElementLayoutOption>
		layoutOptions;
	
	private CssElement
		style,
		mobileStyle;
	
	public WebinterfacePageSection() {
		this.elements = () -> new ArrayList<>();
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
		addDynamicElements(() -> Collections.singletonList(element));
	}
	
	public void addDynamicElements(Supplier<List<WebinterfacePageElement>> elements) {
		Supplier<List<WebinterfacePageElement>> oldEls = this.elements;
		this.elements = () -> {
			List<WebinterfacePageElement> ss = new ArrayList<>(oldEls.get());
			ss.addAll(elements.get());
			return ss;
		};
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
	
	public HtmlElement toHtml() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout page-section");
		for(WebinterfacePageElement e : elements.get()) {
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
