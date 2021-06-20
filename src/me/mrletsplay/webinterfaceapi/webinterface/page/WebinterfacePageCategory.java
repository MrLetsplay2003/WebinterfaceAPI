package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class WebinterfacePageCategory {
	
	private String name;
	private List<WebinterfacePage> pages;
	
	public WebinterfacePageCategory(String name) {
		this.name = name;
		this.pages = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addPage(WebinterfacePage page) {
		pages.add(page);
		if(Webinterface.isInitialized()) Webinterface.getDocumentProvider().registerDocument(page.getUrl(), page);
	}
	
	public List<WebinterfacePage> getPages() {
		return pages;
	}

}
