package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class StringListSetting extends SimpleSetting<List<String>> {

	public StringListSetting(SettingsCategory category, String key, List<String> defaultValue) {
		super(category, key, defaultValue, Complex.list(String.class));
	}
	
}
