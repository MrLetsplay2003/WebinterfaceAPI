package me.mrletsplay.webinterfaceapi.page.impl;

import me.mrletsplay.webinterfaceapi.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.page.SettingsPage;
import me.mrletsplay.webinterfaceapi.page.element.SettingsPane;

public class DefaultSettingsPage extends SettingsPage {
	
	public DefaultSettingsPage() {
		super("Settings", "/wiapi/settings", DefaultPermissions.SETTINGS, new SettingsPane(() -> Webinterface.getConfig(), DefaultSettings.INSTANCE.getSettingsCategories(), "webinterface", "setSetting"));
		setIcon("mdi:cog");
	}

}
