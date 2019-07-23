package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageSection;

public class WebinterfacePage {
	
	private List<WebinterfacePageSection> sections;
	
	public WebinterfacePage() {
		this.sections = new ArrayList<>();
	}
	
	public void addSection(WebinterfacePageSection section) {
		sections.add(section);
	}
	
	public HtmlDocument toHtml() {
		HtmlDocument d = new HtmlDocument();
		for(WebinterfacePageSection s : sections) {
			d.getBodyNode().appendChild(s.toHtml());
		}
		
		d.includeScript("/_internal/include.js", true);
		d.addStyleSheet("/_internal/include.css"); tofododod
		return d;
	}

}
