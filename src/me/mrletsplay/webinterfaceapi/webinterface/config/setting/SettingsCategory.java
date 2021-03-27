package me.mrletsplay.webinterfaceapi.webinterface.config.setting;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl.BooleanSetting;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl.IntSetting;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl.StringListSetting;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl.StringSetting;

public class SettingsCategory {
	
	private String name;
	private List<WebinterfaceSetting<?>> settings;
	
	public SettingsCategory(String name) {
		this.name = name;
		this.settings = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public List<WebinterfaceSetting<?>> getSettings() {
		return settings;
	}
	
	public void addSetting(WebinterfaceSetting<?> setting) {
		settings.add(setting);
	}
	
	public BooleanSetting addBoolean(String path, boolean defaultValue, String friendlyName) {
		BooleanSetting s = new BooleanSetting(this, path, defaultValue);
		s.setFriendlyName(friendlyName);
		addSetting(s);
		return s;
	}
	
	public StringSetting addString(String path, String defaultValue, String friendlyName) {
		StringSetting s = new StringSetting(this, path, defaultValue);
		s.setFriendlyName(friendlyName);
		addSetting(s);
		return s;
	}
	
	public StringListSetting addStringList(String path, List<String> defaultValue, String friendlyName) {
		StringListSetting s = new StringListSetting(this, path, defaultValue);
		s.setFriendlyName(friendlyName);
		addSetting(s);
		return s;
	}
	
	public IntSetting addInt(String path, int defaultValue, String friendlyName) {
		IntSetting s = new IntSetting(this, path, defaultValue);
		s.setFriendlyName(friendlyName);
		addSetting(s);
		return s;
	}
	
}
