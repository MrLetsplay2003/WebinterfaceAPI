package me.mrletsplay.webinterfaceapi.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.webinterfaceapi.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.config.setting.Setting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public interface Config {
	
	public <T> T getSetting(Setting<T> setting);
	
	public <T> void setSetting(Setting<T> setting, T value);
	
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
	
	public default List<Setting<?>> getSettings() {
		return getSettingsCategories().stream()
				.flatMap(c -> c.getSettings().stream())
				.collect(Collectors.toList());
	}
	
	public List<SettingsCategory> getSettingsCategories();
	
	public default Setting<?> getSetting(String key) {
		return getSettings().stream().filter(s -> s.getKey().equals(key)).findFirst().orElse(null);
	}
	
	public <T> T getOverride(String overrideTarget, Class<T> type);
	
	public void setOverride(String overrideTarget, Object override);
	
}
