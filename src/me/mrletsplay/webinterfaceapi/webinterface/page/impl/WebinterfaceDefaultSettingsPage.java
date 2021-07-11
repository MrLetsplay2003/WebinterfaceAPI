package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import me.mrletsplay.webinterfaceapi.webinterface.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfaceSettingsPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceSettingsPane;

public class WebinterfaceDefaultSettingsPage extends WebinterfaceSettingsPage {
	
	public WebinterfaceDefaultSettingsPage() {
		super("Settings", "/wiapi/settings", DefaultPermissions.SETTINGS, new WebinterfaceSettingsPane(() -> Webinterface.getConfig(), DefaultSettings.INSTANCE.getSettingsCategories(), "webinterface", "setSetting"));
	}

}
