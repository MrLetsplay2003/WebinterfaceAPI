package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.webinterfaceapi.css.StyleSheet;
import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.WebinterfaceAccountPage;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfacePage implements HttpDocument {
	
	public static final String
		CONTEXT_PROPERTY_DOCUMENT = "webinterface-document",
		CONTEXT_PROPERTY_SCRIPT = "webinterface-script",
		CONTEXT_PROPERTY_STYLE = "webinterface-style";
	
	private static final Supplier<String> LOGIN_URL = () -> "/login?from=" + HttpUtils.urlEncode(HttpRequestContext.getCurrentContext().getClientHeader().getPath().toString());
	
	private String
		name,
		url,
		permission;
	
	private boolean hidden;
	
	private Supplier<List<WebinterfacePageSection>> sections;
	
	public WebinterfacePage(String name, String url, String permission, boolean hidden) {
		this.name = name;
		this.url = url;
		this.permission = permission;
		this.sections = () -> new ArrayList<>();
		this.hidden = hidden;
	}
	
	public WebinterfacePage(String name, String url, String permission) {
		this(name, url, permission, false);
	}
	
	public WebinterfacePage(String name, String url, boolean hidden) {
		this(name, url, null, hidden);
	}
	
	public WebinterfacePage(String name, String url) {
		this(name, url, null);
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void addDynamicSections(Supplier<List<WebinterfacePageSection>> sections) {
		Supplier<List<WebinterfacePageSection>> oldS = this.sections;
		this.sections = () -> {
			List<WebinterfacePageSection> ss = new ArrayList<>(oldS.get());
			ss.addAll(sections.get());
			return ss;
		};
	}
	
	public void addSection(WebinterfacePageSection section) {
		addDynamicSections(() -> Collections.singletonList(section));
	}
	
	public Supplier<List<WebinterfacePageSection>> getSections() {
		return sections;
	}
	
	public HtmlDocument toHtml() {
		WebinterfaceAccount account = WebinterfaceSession.getCurrentSession().getAccount();
		if(permission != null && !account.hasPermission(permission)) {
			HtmlDocument doc = new HtmlDocument();
			doc.getBodyNode().setText("403 Access denied");
			HttpRequestContext.getCurrentContext().getServerHeader().setStatusCode(HttpStatusCodes.ACCESS_DENIED_403);
			return doc;
		}
		
		HtmlDocument d = new HtmlDocument();
		d.setTitle(name);
		d.setLanguage("en");
		d.includeScript("https://code.jquery.com/jquery-3.5.1.min.js", false, true);
		d.includeScript("/_internal/include.js", false, true);
		JavaScriptScript sc = new JavaScriptScript();
		StyleSheet st = new StyleSheet();
		
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		ctx.setProperty(CONTEXT_PROPERTY_DOCUMENT, d);
		ctx.setProperty(CONTEXT_PROPERTY_SCRIPT, sc);
		ctx.setProperty(CONTEXT_PROPERTY_STYLE, st);
		
		HtmlElement alertBox = new HtmlElement("div");
		alertBox.setID("alert-box");
		d.getBodyNode().appendChild(alertBox);
		
		HtmlElement header = new HtmlElement("header");
		header.addClass("header");

		HtmlElement img2 = HtmlElement.img("/_internal/list.png", "Dropdown");
		img2.addClass("header-list-item");
		img2.setAttribute("onclick", "toggleSidebar()");
		header.appendChild(img2);
		
		HtmlElement img = HtmlElement.img("/_internal/header.png", "WebinterfaceAPI");
		img.addClass("header-image-item");
		img.setSelfClosing(true);
		header.appendChild(img);
		
		HtmlElement ac = new HtmlElement("div");
		ac.addClass("header-avatar");
		ac.setAttribute("onclick", "toggleProfileOptions()");
		header.appendChild(ac);
		
		if(account.getAvatarUrl() != null) {
			HtmlElement av = HtmlElement.img(account.getAvatarUrl(), "User Avatar");
			av.setSelfClosing(true);
			ac.appendChild(av);
		}
		
		HtmlElement usrn = new HtmlElement("a");
		usrn.setText(account.getName());
		ac.appendChild(usrn);
		
		HtmlElement main = new HtmlElement("main");
		main.addClass("main");
		
		HtmlElement content = new HtmlElement("div");
		for(WebinterfacePageSection s : sections.get()) {
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
		lo3.setAttribute("href", WebinterfaceAccountPage.URL);
		lo3.setText("My account");
		loc3.appendChild(lo3);
		
		HtmlElement loc2 = new HtmlElement("div");
		loc2.addClass("profile-option");
		po.appendChild(loc2);
		
		HtmlElement lo2 = new HtmlElement("a");
		lo2.setAttribute("href", LOGIN_URL);
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

		HtmlElement img3 = HtmlElement.img("/_internal/close.png", "Close");
		img3.addClass("header-list-item");
		img3.setAttribute("onclick", "toggleSidebar()");
		sidenav.appendChild(img3);
		
		HtmlElement sideNavList = new HtmlElement("ul");
		sideNavList.addClass("sidenav-list");
		sidenav.appendChild(sideNavList);
		
		for(WebinterfacePage pg : Webinterface.getPages()) {
			appendPageElement(sideNavList, pg);
		}
		
		for(WebinterfacePageCategory category : Webinterface.getCategories()) {
			HtmlElement catEl = new HtmlElement("li");
			catEl.addClass("sidenav-list-category");
			catEl.setText(category.getName());
			sideNavList.appendChild(catEl);
			
			for(WebinterfacePage page : category.getPages()) {
				appendPageElement(sideNavList, page);
			}
		}
		
		// Script minify
		if(Webinterface.getConfig().getSetting(DefaultSettings.MINIFY_SCRIPTS)) minifyScript();
		
		d.getBodyNode().appendChild(header);
		d.getBodyNode().appendChild(main);
		d.getBodyNode().appendChild(sidenav);
		d.getHeadNode().appendChild(HtmlElement.script(sc));
		d.getHeadNode().appendChild(HtmlElement.style(st));
		d.addStyleSheet("/_internal/include.css");
		d.addStyleSheet("/_internal/alerts.css");
		d.addStyleSheet("/_internal/theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		return d;
	}
	
	private void appendPageElement(HtmlElement sideNavList, WebinterfacePage page) {
		WebinterfaceAccount account = WebinterfaceSession.getCurrentSession().getAccount();
		
		if(!page.isHidden() && (page.getPermission() == null || account.hasPermission(page.getPermission()))) {
			HtmlElement sideNavListItem = new HtmlElement("li");
			sideNavListItem.addClass("sidenav-list-item");
			
			HtmlElement a = new HtmlElement("a");
			a.setText(page.getName());
			a.setAttribute("href", page.getUrl());
			sideNavListItem.appendChild(a);
			
			sideNavList.appendChild(sideNavListItem);
		}
	}
	
	private void minifyScript() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		JavaScriptScript sc = (JavaScriptScript) ctx.getProperty(CONTEXT_PROPERTY_SCRIPT);
		
		Map<JavaScriptFunction, String> code = new HashMap<>();
		Map<String, JavaScriptFunction> qFs = new HashMap<>();
		int idx = 0;
		for(JavaScriptFunction f : sc.getFunctions()) {
			String cd = f.getCode().get();
			Map.Entry<JavaScriptFunction, String> fo = code.entrySet().stream().filter(e -> e.getValue().equals(cd)).findFirst().orElse(null);
			if(fo != null) {
				f.setCode("return " + fo.getKey().getSignature() + ";");
			}
			
			Pattern ebip = Pattern.compile("(?<method>(?:document|window|WebinterfaceUtils)\\.[^()\\[\\]]+)\\((?<params>[^\\)]*)\\)");
			
			String rest = cd;
			Matcher m = ebip.matcher(rest);
			StringBuilder nCode = new StringBuilder();
			while(m.find()) {
				JavaScriptFunction of = qFs.get(m.group("method"));
				if(of == null) {
					String sig = "q" + (idx++);
					List<String> args = new ArrayList<>();
					for(int i = 0; i < m.group("params").split(",").length; i++) {
						args.add("arg" + i);
					}
					String aStr = args.stream().collect(Collectors.joining(",", "(", ")"));
					sig += aStr;
					of = new JavaScriptFunction(sig);
					of.setCode("return " + m.group("method") + aStr + ";");
					qFs.put(m.group("method"), of);
				}
				
				nCode.append(rest.substring(0, m.start())); // Append preceding code
				nCode.append(of.getSignature().get().replaceAll("\\([^)]+\\)", "(" + m.group("params") + ")"));
				rest = rest.substring(m.end(), rest.length());
				m = ebip.matcher(rest);
			}
			nCode.append(rest);
			f.setCode(nCode.toString());
		}
		
		qFs.values().forEach(sc::addFunction);
	}

	@Override
	public void createContent() {
		if(WebinterfaceSession.getCurrentSession() == null || WebinterfaceSession.getCurrentSession().getAccount() == null) {
			HttpRequestContext c = HttpRequestContext.getCurrentContext();
			c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			c.getServerHeader().getFields().setFieldValue("Location", LOGIN_URL.get());
			return;
		}
		toHtml().createContent();
	}

}
