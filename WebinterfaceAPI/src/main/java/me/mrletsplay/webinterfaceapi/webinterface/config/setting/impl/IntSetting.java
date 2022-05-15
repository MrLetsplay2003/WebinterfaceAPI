package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class IntSetting extends SimpleSetting<Integer> {

	public IntSetting(SettingsCategory category, String key, int defaultValue) {
		super(category, key, defaultValue, Complex.value(Integer.class));
	}
	
}
