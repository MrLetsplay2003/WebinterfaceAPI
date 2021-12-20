package me.mrletsplay.webinterfaceapi.bukkit.page;

import me.mrletsplay.webinterfaceapi.bukkit.BukkitSettings;
import me.mrletsplay.webinterfaceapi.webinterface.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceSettingsPane;

public class BukkitSettingsPage extends WebinterfacePage {

	public BukkitSettingsPage() {
		super("Settings", "/bukkit/settings", DefaultPermissions.SETTINGS);
		
		WebinterfacePageSection sc2 = new WebinterfacePageSection();
		sc2.setSlimLayout(true);
		sc2.addTitle("Settings");
		sc2.addElement(new WebinterfaceSettingsPane(() -> Webinterface.getConfig(), BukkitSettings.INSTANCE.getSettingsCategories(), "webinterface", "setSetting"));
		
		addSection(sc2);
	}

}
