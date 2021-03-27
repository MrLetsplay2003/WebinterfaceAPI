package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class DoubleListSetting extends SimpleSetting<List<Double>> {

	public DoubleListSetting(SettingsCategory category, String key, List<Double> defaultValue) {
		super(category, key, defaultValue, Complex.list(Double.class));
	}

}
