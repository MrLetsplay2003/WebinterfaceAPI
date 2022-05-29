package me.mrletsplay.webinterfaceapi.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class IntSetting extends AbstractSetting<IntSetting, Integer> {

	private Integer
		min,
		max;

	public IntSetting(SettingsCategory category, String key, int defaultValue) {
		super(category, key, defaultValue, Complex.value(Integer.class));
	}

	public IntSetting min(Integer min) {
		this.min = min;
		return this;
	}

	public Integer getMin() {
		return min;
	}

	public IntSetting max(Integer max) {
		this.max = max;
		return this;
	}

	public Integer getMax() {
		return max;
	}

}
