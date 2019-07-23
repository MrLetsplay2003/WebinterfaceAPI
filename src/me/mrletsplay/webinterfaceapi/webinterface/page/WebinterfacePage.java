package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;

public class WebinterfacePage implements HttpDocument {
	
	public static final String
		PROPERTY_DOCUMENT = "webinterface-document",
		PROPERTY_SCRIPT = "webinterface-script";
	
	private String name;
	private List<WebinterfacePageSection> sections;
	
	public WebinterfacePage(String name) {
		this.name = name;
		this.sections = new ArrayList<>();
	}
	
	public void addSection(WebinterfacePageSection section) {
		sections.add(section);
	}
	
	public HtmlDocument toHtml() {
		HtmlDocument d = new HtmlDocument();
		d.setTitle(name);
		JavaScriptScript sc = new JavaScriptScript();
		
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		ctx.setProperty(PROPERTY_DOCUMENT, d);
		ctx.setProperty(PROPERTY_SCRIPT, sc);
		
		for(WebinterfacePageSection s : sections) {
			d.getBodyNode().appendChild(s.toHtml());
		}
		
		d.getHeadNode().appendChild(HtmlElement.script(sc));
		d.includeScript("/_internal/include.js", true);
		d.addStyleSheet("/_internal/include.css");
		return d;
	}

	@Override
	public void createContent() {
		toHtml().createContent();
	}

}
