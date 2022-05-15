package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import me.mrletsplay.webinterfaceapi.webinterface.page.Page;
import me.mrletsplay.webinterfaceapi.webinterface.page.PageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Text;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.session.Session;

public class WelcomePage extends Page {

	public WelcomePage() {
		super("Welcome", "/wiapi/welcome", true);
		setIcon("mdi:party-popper");

		PageSection sc = new PageSection();
		sc.addTitle(() -> "Welcome to WebinterfaceAPI, " + Session.getCurrentSession().getAccount().getName());
		sc.addElement(Text.builder()
			.text("You can set a custom home page using the \"Home page path\" setting")
			.fullWidth()
			.centerText()
			.create());
		sc.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addSection(sc);
	}

}
