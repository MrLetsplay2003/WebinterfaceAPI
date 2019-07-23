package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class WebinterfacePage implements HttpDocument {
	
	public static final String
		PROPERTY_DOCUMENT = "webinterface-document",
		PROPERTY_SCRIPT = "webinterface-script";
	
	private String name, url;
	private List<WebinterfacePageSection> sections;
	
	public WebinterfacePage(String name, String url) {
		this.name = name;
		this.url = url;
		this.sections = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void addSection(WebinterfacePageSection section) {
		sections.add(section);
	}
	
	public List<WebinterfacePageSection> getSections() {
		return sections;
	}
	
	public HtmlDocument toHtml() {
		HtmlDocument d = new HtmlDocument();
		d.setTitle(name);
		d.setLanguage("en");
		JavaScriptScript sc = new JavaScriptScript();
		
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		ctx.setProperty(PROPERTY_DOCUMENT, d);
		ctx.setProperty(PROPERTY_SCRIPT, sc);
		
		HtmlElement header = new HtmlElement("header");
		header.addClass("header");
		
		HtmlElement img = new HtmlElement("img");
		img.setAttribute("src", "/_internal/header.png");
		header.appendChild(img);
		
		HtmlElement main = new HtmlElement("main");
		main.addClass("main");
		
		HtmlElement content = new HtmlElement("div");
		for(WebinterfacePageSection s : sections) {
			content.appendChild(s.toHtml());
		}
		content.addClass("content-container");
		main.appendChild(content);
		
		HtmlElement sidenav = new HtmlElement("aside");
		sidenav.addClass("sidenav");
		
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
		toHtml().createContent();
	}

}
