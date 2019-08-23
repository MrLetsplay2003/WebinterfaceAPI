package me.mrletsplay.webinterfaceapi.webinterface.config;

import java.util.Arrays;
import java.util.List;

import me.mrletsplay.webinterfaceapi.webinterface.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public interface WebinterfaceConfig {
	
	public <T> T getSetting(WebinterfaceSetting<T> setting);
	
	public <T> void setSetting(WebinterfaceSetting<T> setting, T value);
	
	public void registerSetting(WebinterfaceSetting<?> setting);
	
	public default void registerSettings(List<WebinterfaceSetting<?>> settings) {
		settings.forEach(this::registerSetting);
	}
	
	public default void registerSettings(WebinterfaceSetting<?>... settings) {
		registerSettings(Arrays.asList(settings));
	}
	
	public default void registerSettings(AutoSettings settings) {
		registerSettings(settings.getSettings());
	}
	
	public List<WebinterfaceSetting<?>> getSettings();
	
	public default WebinterfaceSetting<?> getSetting(String key) {
		return getSettings().stream().filter(s -> s.getKey().equals(key)).findFirst().orElse(null);
	}
	
}
