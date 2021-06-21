package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceWelcomePage extends WebinterfacePage {
	
	public WebinterfaceWelcomePage() {
		super("Welcome", "/wiapi/welcome");
		
		WebinterfacePageSection sc = new WebinterfacePageSection();
		sc.addTitle(() -> "Welcome to WebinterfaceAPI, " + WebinterfaceSession.getCurrentSession().getAccount().getName());
		WebinterfaceText tx = new WebinterfaceText("Set up a custom home page using the \"Home page path\" setting");
		tx.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH, DefaultLayoutProperty.CENTER_VERTICALLY, DefaultLayoutProperty.CENTER_TEXT);
		sc.addElement(tx);
		sc.addInnerLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
		addSection(sc);
	}
	
}
