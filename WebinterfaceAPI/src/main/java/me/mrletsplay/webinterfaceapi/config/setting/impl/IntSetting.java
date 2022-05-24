package me.mrletsplay.webinterfaceapi.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class IntSetting extends AbstractSetting<IntSetting, Integer> {

	public IntSetting(SettingsCategory category, String key, int defaultValue) {
		super(category, key, defaultValue, Complex.value(Integer.class));
	}

}
