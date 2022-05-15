package me.mrletsplay.webinterfaceapi.bukkit.page;

import me.mrletsplay.webinterfaceapi.bukkit.BukkitSettings;
import me.mrletsplay.webinterfaceapi.webinterface.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.Page;
import me.mrletsplay.webinterfaceapi.webinterface.page.PageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.SettingsPane;

public class BukkitSettingsPage extends Page {

	public BukkitSettingsPage() {
		super("Settings", "/bukkit/settings", DefaultPermissions.SETTINGS);
		
		PageSection sc2 = new PageSection();
		sc2.setSlimLayout(true);
		sc2.addTitle("Settings");
		sc2.addElement(new SettingsPane(() -> Webinterface.getConfig(), BukkitSettings.INSTANCE.getSettingsCategories(), "webinterface", "setSetting"));
		
		addSection(sc2);
	}

}
