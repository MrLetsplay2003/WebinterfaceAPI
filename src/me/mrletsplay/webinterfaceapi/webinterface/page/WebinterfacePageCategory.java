package me.mrletsplay.webinterfaceapi.webinterface.page;

import java.util.ArrayList;
import java.util.List;

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
	}
	
	public List<WebinterfacePage> getPages() {
		return pages;
	}

}
