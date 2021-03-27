package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class StringSetting extends SimpleSetting<String> {

	public StringSetting(SettingsCategory category, String key, String defaultValue) {
		super(category, key, defaultValue, Complex.value(String.class));
	}
	
}
