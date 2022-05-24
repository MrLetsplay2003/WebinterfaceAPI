package me.mrletsplay.webinterfaceapi.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class DoubleListSetting extends AbstractSetting<DoubleListSetting, List<Double>> {

	public DoubleListSetting(SettingsCategory category, String key, List<Double> defaultValue) {
		super(category, key, defaultValue, Complex.list(Double.class));
	}

}
