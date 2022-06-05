package me.mrletsplay.webinterfaceapi.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.mrletsplay.simplehttpserver.dom.css.CssElement;
import me.mrletsplay.simplehttpserver.dom.css.CssSelector;
import me.mrletsplay.simplehttpserver.dom.css.StyleSheet;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.context.WebinterfaceContext;
import me.mrletsplay.webinterfaceapi.page.dynamic.DynamicContent;
import me.mrletsplay.webinterfaceapi.page.dynamic.DynamicList;
import me.mrletsplay.webinterfaceapi.page.dynamic.DynamicMultiple;
import me.mrletsplay.webinterfaceapi.page.dynamic.DynamicOptional;
import me.mrletsplay.webinterfaceapi.page.element.Heading;
import me.mrletsplay.webinterfaceapi.page.element.PageElement;
import me.mrletsplay.webinterfaceapi.page.element.TitleText;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.page.element.layout.ElementLayoutOption;
import me.mrletsplay.webinterfaceapi.page.element.layout.Grid;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class PageSection {

	private String id;

	private DynamicList<PageElement> elementsList;

	private List<ElementLayoutOption>
		layoutOptions;

	private CssElement
		style,
		mobileStyle;

	private boolean slimLayout;

	public PageSection() {
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

	public void addElement(PageElement element) {
		elementsList.addStatic(element);
	}

	public void dynamic(DynamicContent<PageElement> dynamic) {
		elementsList.addDynamic(dynamic);
	}

	public void dynamic(DynamicMultiple<PageElement> dynamic) {
		elementsList.addDynamicMultiple(dynamic);
	}

	public void dynamic(DynamicOptional<PageElement> dynamic) {
		elementsList.addDynamicOptional(dynamic);
	}

	public void addTitle(Supplier<String> title) {
		TitleText tt = new TitleText(title);
		tt.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH, DefaultLayoutOption.CENTER_TEXT);
		addElement(tt);
	}

	public void addTitle(String title) {
		addTitle(() -> title);
	}

	public void addHeading(Supplier<String> title, int level) {
		Heading h = new Heading(title);
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
	 * This should only be used if this is the only section on the page
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

	public void setGrid(Grid grid) {
		getStyle().setProperty("grid-template-columns", grid.getColumns() == null ? null : Arrays.stream(grid.getColumns()).collect(Collectors.joining(" ")));
		getStyle().setProperty("grid-template-rows", grid.getRows() == null ? null : Arrays.stream(grid.getRows()).collect(Collectors.joining(" ")));
		getStyle().setProperty("grid-gap", grid.getGap());
	}

	public HtmlElement toHtml() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout page-section");

		if(slimLayout) {
			el.addClass("page-section-slim");
		}

		for(PageElement e : elementsList.create()) {
			if(e.isTemplate()) throw new IllegalStateException("Page contains template elements");
			el.appendChild(e.toHtml());
		}

		WebinterfaceContext ctx = WebinterfaceContext.getCurrentContext();
		StyleSheet st = ctx.getStyle();
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
