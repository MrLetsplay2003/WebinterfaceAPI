package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import me.mrletsplay.webinterfaceapi.webinterface.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAfterAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceSettingsPane;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;

public class WebinterfaceSettingsPage extends WebinterfacePage {
	
	public WebinterfaceSettingsPage() {
		super("Settings", "/wiapi/settings", DefaultPermissions.SETTINGS);
		
		WebinterfacePageSection sc2 = new WebinterfacePageSection();
		sc2.addTitle("Settings");
		sc2.addElement(new WebinterfaceSettingsPane(() -> Webinterface.getConfig(), DefaultSettings.INSTANCE.getSettings(), "webinterface", "setSetting"));
		
		WebinterfaceButton btn = new WebinterfaceButton("Restart");
		btn.addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
		btn.setOnClickAction(new MultiAction(
				new SendJSAction("webinterface", "restart", null),
				new ReloadPageAfterAction(1000)));
		sc2.addElement(btn);
		
		addSection(sc2);
	}

}
