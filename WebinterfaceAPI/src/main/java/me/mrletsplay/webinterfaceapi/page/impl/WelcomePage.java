package me.mrletsplay.webinterfaceapi.page.impl;

import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;
import me.mrletsplay.webinterfaceapi.page.element.Text;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.session.Session;

public class WelcomePage extends Page {

	public WelcomePage() {
		super("Welcome", "/wiapi/welcome", true);
		setIcon("mdi:party-popper");

		PageSection sc = new PageSection();
		sc.addTitle(() -> "Welcome to WebinterfaceAPI, " + Session.getCurrentSession().getAccount().getUsername());
		sc.addElement(Text.builder()
			.text("You can set a custom home page using the \"Home page path\" setting")
			.fullWidth()
			.centerText()
			.create());
		sc.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addSection(sc);
	}

}
