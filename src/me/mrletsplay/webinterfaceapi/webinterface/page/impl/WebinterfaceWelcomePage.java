package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceWelcomePage extends WebinterfacePage {
	
	public WebinterfaceWelcomePage() {
		super("Welcome", "/wiapi/welcome");
		setIcon("mdi:party-popper");
		
		WebinterfacePageSection sc = new WebinterfacePageSection();
		sc.addTitle(() -> "Welcome to WebinterfaceAPI, " + WebinterfaceSession.getCurrentSession().getAccount().getName());
		WebinterfaceText tx = new WebinterfaceText("You can set up a custom home page using the \"Home page path\" setting");
		tx.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH, DefaultLayoutOption.CENTER_VERTICALLY, DefaultLayoutOption.CENTER_TEXT);
		sc.addElement(tx);
		sc.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addSection(sc);
	}
	
}
