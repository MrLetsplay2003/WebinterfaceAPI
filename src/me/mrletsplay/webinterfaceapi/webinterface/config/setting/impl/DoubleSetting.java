package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class DoubleSetting extends SimpleSetting<Double> {

	public DoubleSetting(SettingsCategory category, String key, double defaultValue) {
		super(category, key, defaultValue, Complex.value(Double.class));
	}
	
}
