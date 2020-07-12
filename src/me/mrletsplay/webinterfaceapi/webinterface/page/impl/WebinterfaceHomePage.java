package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceHomePage extends WebinterfacePage {
	
	public WebinterfaceHomePage() {
		super("Home", "/");
		
		WebinterfacePageSection sc = new WebinterfacePageSection();
		sc.addTitle(() -> "Welcome to WebinterfaceAPI, " + WebinterfaceSession.getCurrentSession().getAccount().getName());
		WebinterfaceText tx = new WebinterfaceText("Hello World!");
		tx.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH, DefaultLayoutProperty.CENTER_VERTICALLY, DefaultLayoutProperty.CENTER_TEXT);
		sc.addElement(tx);
		sc.addInnerLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
		addSection(sc);
	}
	
	public WebinterfacePageSection createSection() {
		WebinterfacePageSection sec = new WebinterfacePageSection();
		addSection(sec);
		return sec;
	}

}
