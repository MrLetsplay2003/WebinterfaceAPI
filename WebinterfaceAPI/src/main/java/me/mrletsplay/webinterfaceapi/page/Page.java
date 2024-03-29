package me.mrletsplay.webinterfaceapi.page;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.simplehttpserver.dom.css.CssElement;
import me.mrletsplay.simplehttpserver.dom.css.CssSelector;
import me.mrletsplay.simplehttpserver.dom.css.StyleSheet;
import me.mrletsplay.simplehttpserver.dom.html.HtmlDocument;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.dom.js.JSScript;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.context.WebinterfaceContext;
import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.page.dynamic.DynamicContent;
import me.mrletsplay.webinterfaceapi.page.dynamic.DynamicList;
import me.mrletsplay.webinterfaceapi.page.dynamic.DynamicMultiple;
import me.mrletsplay.webinterfaceapi.page.dynamic.DynamicOptional;
import me.mrletsplay.webinterfaceapi.page.impl.AccountPage;
import me.mrletsplay.webinterfaceapi.session.Session;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class Page implements HttpDocument {

	private String
		name,
		url,
		permission;

	private boolean hidden;

	private String icon;

	private DynamicList<PageSection> sectionsList;

	private List<PeriodicAction> periodicActions;

	private CssElement
		containerStyle,
		mobileContainerStyle;

	public Page(String name, String url, String permission, boolean hidden) {
		this.name = name;
		this.url = url;
		this.permission = permission;
		this.sectionsList = new DynamicList<>();
		this.periodicActions = new ArrayList<>();
		this.hidden = hidden;
		this.containerStyle = new CssElement(CssSelector.selectClass("content-container"));
		this.mobileContainerStyle = new CssElement(CssSelector.selectClass("content-container"));
	}

	public Page(String name, String url, String permission) {
		this(name, url, permission, false);
	}

	public Page(String name, String url, boolean hidden) {
		this(name, url, null, hidden);
	}

	public Page(String name, String url) {
		this(name, url, null);
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public CssElement getContainerStyle() {
		return containerStyle;
	}

	public CssElement getMobileContainerStyle() {
		return mobileContainerStyle;
	}

	public void dynamic(DynamicContent<PageSection> dynamic) {
		sectionsList.addDynamic(dynamic);
	}

	public void dynamic(DynamicMultiple<PageSection> dynamic) {
		sectionsList.addDynamicMultiple(dynamic);
	}

	public void dynamic(DynamicOptional<PageSection> dynamic) {
		sectionsList.addDynamicOptional(dynamic);
	}

	public void addSection(PageSection section) {
		sectionsList.addStatic(section);
	}

	public DynamicList<PageSection> getSectionsList() {
		return sectionsList;
	}

	public void addPeriodicAction(PeriodicAction action) {
		periodicActions.add(action);
	}

	public List<PeriodicAction> getPeriodicActions() {
		return periodicActions;
	}

	public HtmlDocument toHtml() {
		Account account = Session.getCurrentSession().getAccount();
		if(permission != null && !account.hasPermission(permission)) {
			HtmlDocument doc = new HtmlDocument();
			doc.getBodyNode().setText("403 Access denied");
			HttpRequestContext.getCurrentContext().getServerHeader().setStatusCode(HttpStatusCodes.ACCESS_DENIED_403);
			return doc;
		}

		HtmlDocument d = new HtmlDocument();
		d.setTitle(name);
		d.setIcon("/_internal/include/img/" + Webinterface.getConfig().getSetting(DefaultSettings.ICON_IMAGE));
		d.setLanguage("en");

		for(String script : WebinterfaceUtils.DEFAULT_SCRIPTS) {
			d.includeScript(script, false, true);
		}
		JSScript sc = new JSScript();
		StringBuilder b = new StringBuilder();
		for(PeriodicAction a : periodicActions) {
			b.append(String.format("setInterval(() => {%s}, %s);", a.getAction().getCode(), a.getPeriodMillis()));
			if(a.isRunImmediately()) b.append(a.getAction().getCode());
		}
		sc.appendCode(String.format("window.addEventListener('DOMContentLoaded', () => {%s});", b.toString()));
		StyleSheet st = new StyleSheet();
		st.addElement(containerStyle);
		st.addMobileElement(mobileContainerStyle);

		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		WebinterfaceContext wiCtx = new WebinterfaceContext(ctx, d, sc, st);
		wiCtx.requireModule(DefaultJSModule.BASE);
		wiCtx.requireModule(DefaultJSModule.TOAST);
		ctx.setProperty(WebinterfaceContext.CONTEXT_PROPERTY_NAME, wiCtx);

		HtmlElement alertBox = new HtmlElement("div");
		alertBox.setID("alert-box");
		d.getBodyNode().appendChild(alertBox);

		HtmlElement loadingBox = new HtmlElement("div");
		loadingBox.setID("loading-box");

		HtmlElement b1 = new HtmlElement("div");
		b1.addClass("bounce1");
		loadingBox.appendChild(b1);
		HtmlElement b2 = new HtmlElement("div");
		b2.addClass("bounce2");
		loadingBox.appendChild(b2);
		HtmlElement b3 = new HtmlElement("div");
		b3.addClass("bounce3");
		loadingBox.appendChild(b3);

		d.getBodyNode().appendChild(loadingBox);

		HtmlElement header = new HtmlElement("header");
		header.addClass("header");

		HtmlElement img2 = HtmlElement.img("/_internal/include/img/list.svg", "Dropdown");
		img2.addClass("header-list-item");
		img2.setAttribute("onclick", "toggleSidebar()");
		header.appendChild(img2);

		HtmlElement img = HtmlElement.img("/_internal/include/img/" + Webinterface.getConfig().getSetting(DefaultSettings.HEADER_IMAGE), "WebinterfaceAPI");
		img.addClass("header-image-item");
		img.setSelfClosing(true);
		img.setAttribute("onclick", "window.location.href = \"/\";");
		header.appendChild(img);

		HtmlElement ac = new HtmlElement("div");
		ac.addClass("header-avatar");
		ac.setAttribute("onclick", "toggleProfileOptions()");
		header.appendChild(ac);

		if(account.getAvatar() != null) {
			HtmlElement av = HtmlElement.img(account.getAvatar(), "User Avatar");
			av.setSelfClosing(true);
			ac.appendChild(av);
		}

		HtmlElement usrn = new HtmlElement("a");
		usrn.setText(account.getUsername());
		ac.appendChild(usrn);

		HtmlElement main = new HtmlElement("main");
		main.addClass("main");

		HtmlElement content = new HtmlElement("div");
		for(PageSection s : sectionsList.create()) {
			content.appendChild(s.toHtml());
		}
		content.addClass("content-container");
		main.appendChild(content);

		HtmlElement po = new HtmlElement("div");
		po.addClass("profile-options");
		main.appendChild(po);

		HtmlElement loc3 = new HtmlElement("div");
		loc3.addClass("profile-option");
		po.appendChild(loc3);

		HtmlElement lo3 = new HtmlElement("a");
		lo3.setAttribute("href", AccountPage.URL);
		lo3.setText("My account");
		loc3.appendChild(lo3);

		HtmlElement loc2 = new HtmlElement("div");
		loc2.addClass("profile-option");
		po.appendChild(loc2);

		HtmlElement lo2 = new HtmlElement("a");
		lo2.setAttribute("href", Session.LOGIN_URL);
		lo2.setText("Switch accounts");
		loc2.appendChild(lo2);

		HtmlElement loc = new HtmlElement("div");
		loc.addClass("profile-option");
		po.appendChild(loc);

		HtmlElement lo = new HtmlElement("a");
		lo.setAttribute("href", "/logout");
		lo.setText("Log Out");
		loc.appendChild(lo);

		HtmlElement sidenav = new HtmlElement("aside");
		sidenav.addClass("sidenav");

		HtmlElement img3 = HtmlElement.img("/_internal/include/img/close.svg", "Close");
		img3.addClass("header-list-item");
		img3.setAttribute("onclick", "toggleSidebar()");
		sidenav.appendChild(img3);

		HtmlElement sideNavList = new HtmlElement("ul");
		sideNavList.addClass("sidenav-list");
		sidenav.appendChild(sideNavList);

		for(Page pg : Webinterface.getPages()) {
			appendPageElement(sideNavList, pg);
		}

		for(PageCategory category : Webinterface.getCategories()) {
			if(category.getPages().stream().allMatch(p -> p.isHidden() || (p.getPermission() != null && !account.hasPermission(p.getPermission())))) continue;
			HtmlElement catEl = new HtmlElement("li");
			catEl.addClass("sidenav-list-category");
			catEl.setText(category.getName());
			sideNavList.appendChild(catEl);

			for(Page page : category.getPages()) {
				appendPageElement(sideNavList, page);
			}
		}

		d.getBodyNode().appendChild(header);
		d.getBodyNode().appendChild(main);
		d.getBodyNode().appendChild(sidenav);
		d.getHeadNode().appendChild(HtmlElement.script(sc));

		wiCtx.includeStyleSheet("base.css");
		wiCtx.includeStyleSheet("include.css");
		wiCtx.includeStyleSheet("alerts.css");
		wiCtx.includeStyleSheet("theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		d.getHeadNode().appendChild(HtmlElement.style(st));
		for(JSModule m : wiCtx.getRequiredModules()) {
			d.includeScript("/_internal/module/" + m.getIdentifier() + ".js", false, true);
		}
		return d;
	}

	private void appendPageElement(HtmlElement sideNavList, Page page) {
		Account account = Session.getCurrentSession().getAccount();

		if(!page.isHidden() && (page.getPermission() == null || account.hasPermission(page.getPermission()))) {
			HtmlElement sideNavListItem = new HtmlElement("li");
			sideNavListItem.addClass("sidenav-list-item");

			HtmlElement wrapper = new HtmlElement("a");
			wrapper.addClass("sidenav-wrapper");
			wrapper.setAttribute("href", page.getUrl());

			HtmlElement iconDiv = new HtmlElement("div");
			iconDiv.addClass("sidenav-icon");

			String iconName = page.getIcon();
			if(iconName == null) iconName = "mdi:chevron-right";
			HtmlElement icon = new HtmlElement("span");
			icon.addClass("iconify");
			icon.setAttribute("data-icon", iconName);
			iconDiv.appendChild(icon);

			wrapper.appendChild(iconDiv);

			HtmlElement label = new HtmlElement("div");
			label.addClass("sidenav-label");
			label.setText(page.getName());
			wrapper.appendChild(label);

			sideNavListItem.appendChild(wrapper);

			sideNavList.appendChild(sideNavListItem);
		}
	}

	@Override
	public void createContent() {
		if(!Session.requireSession()) return;
		toHtml().createContent();
	}

}
