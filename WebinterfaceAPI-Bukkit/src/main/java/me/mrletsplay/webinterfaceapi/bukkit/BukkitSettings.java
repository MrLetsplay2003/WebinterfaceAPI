package me.mrletsplay.webinterfaceapi.bukkit;

import java.util.Arrays;

import me.mrletsplay.webinterfaceapi.config.setting.AutoSetting;
import me.mrletsplay.webinterfaceapi.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.config.setting.impl.BooleanSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.StringListSetting;

public class BukkitSettings implements AutoSettings {
	
	public static final BukkitSettings INSTANCE = new BukkitSettings();
	
	@AutoSetting
	private static SettingsCategory homePage = new SettingsCategory("Home Page");
	
	public static final BooleanSetting
		SHOW_STATS = homePage.addBoolean("bukkit.home.stats.show", false, "Show stats");
	
	public static final StringListSetting
		STATS_ELEMENTS = homePage.addStringList("bukkit.home.stats.elements", Arrays.asList("Rank:%vault_rank%", "Play Time:%playtime_time%"), "Stats elements", "Elements for the stats section of the Home page in the format of \"Name:Value with %placeholders%\"");

}
