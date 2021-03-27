package me.mrletsplay.webinterfaceapi.webinterface.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.webinterfaceapi.webinterface.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public interface WebinterfaceConfig {
	
	public <T> T getSetting(WebinterfaceSetting<T> setting);
	
	public <T> void setSetting(WebinterfaceSetting<T> setting, T value);
	
	public void registerSettings(SettingsCategory category);
	
	public default void registerSettings(List<SettingsCategory> categories) {
		categories.forEach(this::registerSettings);
	}
	
	public default void registerSettings(SettingsCategory... categories) {
		registerSettings(Arrays.asList(categories));
	}
	
	public default void registerSettings(AutoSettings settings) {
		registerSettings(settings.getSettingsCategories());
	}
	
	public default List<WebinterfaceSetting<?>> getSettings() {
		return getSettingsCategories().stream()
				.flatMap(c -> c.getSettings().stream())
				.collect(Collectors.toList());
	}
	
	public List<SettingsCategory> getSettingsCategories();
	
	public default WebinterfaceSetting<?> getSetting(String key) {
		return getSettings().stream().filter(s -> s.getKey().equals(key)).findFirst().orElse(null);
	}
	
	public <T> T getOverride(String overrideTarget, Class<T> type);
	
	public void setOverride(String overrideTarget, Object override);
	
}
