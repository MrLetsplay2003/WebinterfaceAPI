package me.mrletsplay.webinterfaceapi.page;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceState;

public class PageCategory {

	private String name;
	private List<Page> pages;

	public PageCategory(String name) {
		this.name = name;
		this.pages = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void addPage(Page page) {
		pages.add(page);
		if(Webinterface.getState() == WebinterfaceState.RUNNING) Webinterface.getDocumentProvider().registerDocument(page.getUrl(), page);
	}

	public List<Page> getPages() {
		return pages;
	}

}
