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
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfacePage implements HttpDocument {
	
	public static final String
		CONTEXT_PROPERTY_DOCUMENT = "webinterface-document",
		CONTEXT_PROPERTY_SCRIPT = "webinterface-script";
	
	private String name, url, permission;
	private Supplier<List<WebinterfacePageSection>> sections;
	
	public WebinterfacePage(String name, String url, String permission) {
		this.name = name;
		this.url = url;
		this.permission = permission;
		this.sections = () -> new ArrayList<>();
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
		JavaScriptScript sc = new JavaScriptScript();
		
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		ctx.setProperty(CONTEXT_PROPERTY_DOCUMENT, d);
		ctx.setProperty(CONTEXT_PROPERTY_SCRIPT, sc);
		
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
		
		HtmlElement loc2 = new HtmlElement("div");
		loc2.addClass("profile-option");
		po.appendChild(loc2);
		
		HtmlElement lo2 = new HtmlElement("a");
		lo2.setAttribute("href", "/login");
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
			if(pg.getPermission() == null || account.hasPermission(pg.getPermission())) {
				HtmlElement sideNavListItem = new HtmlElement("li");
				sideNavListItem.addClass("sidenav-list-item");
				
				HtmlElement a = new HtmlElement("a");
				a.setText(pg.getName());
				a.setAttribute("href", pg.getUrl());
				sideNavListItem.appendChild(a);
				
				sideNavList.appendChild(sideNavListItem);
			}
		}
		
		// Script minify
		if(Webinterface.getConfig().getSetting(DefaultSettings.MINIFY_SCRIPTS)) {
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
		
		d.getBodyNode().appendChild(header);
		d.getBodyNode().appendChild(main);
		d.getBodyNode().appendChild(sidenav);
		d.getHeadNode().appendChild(HtmlElement.script(sc));
		d.includeScript("/_internal/include.js", true);
		d.includeScript("https://code.jquery.com/jquery-3.4.1.min.js", true);
		d.addStyleSheet("/_internal/include.css");
		d.addStyleSheet("/_internal/theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		return d;
	}

	@Override
	public void createContent() {
		if(WebinterfaceSession.getCurrentSession() == null || WebinterfaceSession.getCurrentSession().getAccount() == null) {
			HttpRequestContext c = HttpRequestContext.getCurrentContext();
			c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			c.getServerHeader().getFields().setFieldValue("Location", "/login");
			return;
		}
		toHtml().createContent();
	}

}
