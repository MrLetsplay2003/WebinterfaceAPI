package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class BooleanSetting extends SimpleSetting<Boolean> {

	public BooleanSetting(SettingsCategory category, String key, boolean defaultValue) {
		super(category, key, defaultValue, Complex.value(Boolean.class));
	}

}
