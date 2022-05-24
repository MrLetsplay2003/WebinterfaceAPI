package me.mrletsplay.webinterfaceapi.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class BooleanSetting extends AbstractSetting<BooleanSetting, Boolean> {

	public BooleanSetting(SettingsCategory category, String key, boolean defaultValue) {
		super(category, key, defaultValue, Complex.value(Boolean.class));
	}

}
