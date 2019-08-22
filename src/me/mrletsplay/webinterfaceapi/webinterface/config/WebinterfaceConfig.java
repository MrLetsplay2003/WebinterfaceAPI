package me.mrletsplay.webinterfaceapi.webinterface.config;

import java.util.Arrays;
import java.util.List;

import me.mrletsplay.webinterfaceapi.webinterface.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public interface WebinterfaceConfig {
	
	public String getStringSetting(WebinterfaceSetting<String> setting);
	
	public boolean getBooleanSetting(WebinterfaceSetting<Boolean> setting);

	public int getIntSetting(WebinterfaceSetting<Integer> setting);

	public double getDoubleSetting(WebinterfaceSetting<Double> setting);
	
	public List<String> getStringListSetting(WebinterfaceSetting<List<String>> setting);
	
	public List<Boolean> getBooleanListSetting(WebinterfaceSetting<List<Boolean>> setting);

	public List<Integer> getIntListSetting(WebinterfaceSetting<List<Integer>> setting);

	public List<Double> getDoubleListSetting(WebinterfaceSetting<List<Double>> setting);
	
	public <T> void setSetting(WebinterfaceSetting<T> setting, T value);
	
	public void initializeSetting(WebinterfaceSetting<?> setting);
	
	public default void initializeSettings(List<WebinterfaceSetting<?>> settings) {
		settings.forEach(this::initializeSetting);
	}
	
	public default void initializeSettings(WebinterfaceSetting<?>... settings) {
		initializeSettings(Arrays.asList(settings));
	}
	
	public default void initializeSettings(AutoSettings settings) {
		initializeSettings(settings.getSettings());
	}
	
}
