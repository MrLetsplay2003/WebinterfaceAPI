package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceWelcomePage extends WebinterfacePage {
	
	public WebinterfaceWelcomePage() {
		super("Welcome", "/wiapi/welcome", true);
		setIcon("mdi:party-popper");
		
		WebinterfacePageSection sc = new WebinterfacePageSection();
		sc.addTitle(() -> "Welcome to WebinterfaceAPI, " + WebinterfaceSession.getCurrentSession().getAccount().getName());
		sc.addElement(WebinterfaceText.builder()
				.text("You can set a custom home page using the \"Home page path\" setting")
				.fullWidth()
				.centerText()
				.create());
		sc.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addSection(sc);
	}
	
}
