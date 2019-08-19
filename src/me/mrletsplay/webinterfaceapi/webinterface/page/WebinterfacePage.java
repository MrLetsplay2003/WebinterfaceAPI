package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfacePage implements HttpDocument {
	
	public static final String
		CONTEXT_PROPERTY_DOCUMENT = "webinterface-document",
		CONTEXT_PROPERTY_SCRIPT = "webinterface-script";
	
	private String name, url;
	private Supplier<List<WebinterfacePageSection>> sections;
	
	public WebinterfacePage(String name, String url) {
		this.name = name;
		this.url = url;
		this.sections = () -> new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
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
		HtmlDocument d = new HtmlDocument();
		d.setTitle(name);
		d.setLanguage("en");
		JavaScriptScript sc = new JavaScriptScript();
		
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		ctx.setProperty(CONTEXT_PROPERTY_DOCUMENT, d);
		ctx.setProperty(CONTEXT_PROPERTY_SCRIPT, sc);
		
		HtmlElement header = new HtmlElement("header");
		header.addClass("header");

		HtmlElement img2 = new HtmlElement("img");
		img2.setAttribute("src", "/_internal/list.png");
		img2.setAttribute("alt", "Dropdown");
		img2.addClass("header-list-item");
		img2.setAttribute("onclick", "toggleSidebar()");
		header.appendChild(img2);
		
		HtmlElement img = new HtmlElement("img");
		img.setAttribute("src", "/_internal/header.png");
		img.setAttribute("alt", "WebinterfaceAPI");
		img.addClass("header-image-item");
		header.appendChild(img);
		
		HtmlElement main = new HtmlElement("main");
		main.addClass("main");
		
		HtmlElement content = new HtmlElement("div");
		for(WebinterfacePageSection s : sections.get()) {
			content.appendChild(s.toHtml());
		}
		content.addClass("content-container");
		main.appendChild(content);
		
		HtmlElement sidenav = new HtmlElement("aside");
		sidenav.addClass("sidenav");

		HtmlElement img3 = new HtmlElement("img");
		img3.setAttribute("src", "/_internal/close.png");
		img3.setAttribute("alt", "Close");
		img3.addClass("header-list-item");
		img3.setAttribute("onclick", "toggleSidebar()");
		sidenav.appendChild(img3);
		
		HtmlElement sideNavList = new HtmlElement("ul");
		sideNavList.addClass("sidenav-list");
		sidenav.appendChild(sideNavList);
		
		for(WebinterfacePage pg : Webinterface.getPages()) {
			HtmlElement sideNavListItem = new HtmlElement("li");
			sideNavListItem.addClass("sidenav-list-item");
			
			HtmlElement a = new HtmlElement("a");
			a.setText(pg.getName());
			a.setAttribute("href", pg.getUrl());
			sideNavListItem.appendChild(a);
			
			sideNavList.appendChild(sideNavListItem);
		}
		
		d.getBodyNode().appendChild(header);
		d.getBodyNode().appendChild(main);
		d.getBodyNode().appendChild(sidenav);
		d.getHeadNode().appendChild(HtmlElement.script(sc));
		d.includeScript("/_internal/include.js", true);
		d.includeScript("https://code.jquery.com/jquery-3.4.1.min.js", true);
		d.addStyleSheet("/_internal/include.css");
		return d;
	}

	@Override
	public void createContent() {
		if(WebinterfaceSession.getCurrentSession() == null) {
			HttpRequestContext c = HttpRequestContext.getCurrentContext();
			c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			c.getServerHeader().getFields().setFieldValue("Location", "/login");
			return;
		}
		toHtml().createContent();
	}

}
