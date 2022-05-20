package me.mrletsplay.webinterfaceapi.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.config.setting.SimpleSetting;

public class StringSetting extends SimpleSetting<String> {

	public StringSetting(SettingsCategory category, String key, String defaultValue) {
		super(category, key, defaultValue, Complex.value(String.class));
	}
	
}
