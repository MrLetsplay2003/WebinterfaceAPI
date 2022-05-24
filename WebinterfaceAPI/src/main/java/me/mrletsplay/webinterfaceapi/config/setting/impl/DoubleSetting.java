package me.mrletsplay.webinterfaceapi.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class DoubleSetting extends AbstractSetting<DoubleSetting, Double> {

	public DoubleSetting(SettingsCategory category, String key, double defaultValue) {
		super(category, key, defaultValue, Complex.value(Double.class));
	}

}
