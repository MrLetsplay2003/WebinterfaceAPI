package me.mrletsplay.webinterfaceapi.webinterface.page.impl;

import me.mrletsplay.webinterfaceapi.webinterface.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.webinterface.page.SettingsPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.SettingsPane;

public class DefaultSettingsPage extends SettingsPage {
	
	public DefaultSettingsPage() {
		super("Settings", "/wiapi/settings", DefaultPermissions.SETTINGS, new SettingsPane(() -> Webinterface.getConfig(), DefaultSettings.INSTANCE.getSettingsCategories(), "webinterface", "setSetting"));
		setIcon("mdi:cog");
	}

}
