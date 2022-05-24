package me.mrletsplay.webinterfaceapi.config.setting;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.config.setting.impl.BooleanSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.IntSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.StringListSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.StringSetting;

public class SettingsCategory {
	
	private String name;
	private List<Setting<?>> settings;
	
	public SettingsCategory(String name) {
		this.name = name;
		this.settings = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public List<Setting<?>> getSettings() {
		return settings;
	}
	
	public void addSetting(Setting<?> setting) {
		settings.add(setting);
	}
	
	public BooleanSetting addBoolean(String path, boolean defaultValue, String friendlyName, String description) {
		BooleanSetting s = new BooleanSetting(this, path, defaultValue);
		s.friendlyName(friendlyName);
		if(description != null) s.description(description);
		addSetting(s);
		return s;
	}
	
	public BooleanSetting addBoolean(String path, boolean defaultValue, String friendlyName) {
		return addBoolean(path, defaultValue, friendlyName, null);
	}
	
	public StringSetting addString(String path, String defaultValue, String friendlyName, String description) {
		StringSetting s = new StringSetting(this, path, defaultValue);
		s.friendlyName(friendlyName);
		if(description != null) s.description(description);
		addSetting(s);
		return s;
	}
	
	public StringSetting addString(String path, String defaultValue, String friendlyName) {
		return addString(path, defaultValue, friendlyName, null);
	}
	
	public StringListSetting addStringList(String path, List<String> defaultValue, String friendlyName, String description) {
		StringListSetting s = new StringListSetting(this, path, defaultValue);
		s.friendlyName(friendlyName);
		if(description != null) s.description(description);
		addSetting(s);
		return s;
	}
	
	public StringListSetting addStringList(String path, List<String> defaultValue, String friendlyName) {
		return addStringList(path, defaultValue, friendlyName, null);
	}
	
	public IntSetting addInt(String path, int defaultValue, String friendlyName, String description) {
		IntSetting s = new IntSetting(this, path, defaultValue);
		s.friendlyName(friendlyName);
		if(description != null) s.description(description);
		addSetting(s);
		return s;
	}
	
	public IntSetting addInt(String path, int defaultValue, String friendlyName) {
		return addInt(path, defaultValue, friendlyName, null);
	}
	
}
