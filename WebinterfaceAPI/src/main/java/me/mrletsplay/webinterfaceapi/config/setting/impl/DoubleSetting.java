package me.mrletsplay.webinterfaceapi.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class DoubleSetting extends AbstractSetting<DoubleSetting, Double> {

	private Double
		min,
		max;

	public DoubleSetting(SettingsCategory category, String key, double defaultValue) {
		super(category, key, defaultValue, Complex.value(Double.class));
	}

	public DoubleSetting min(Double min) {
		this.min = min;
		return this;
	}

	public Double getMin() {
		return min;
	}

	public DoubleSetting max(Double max) {
		this.max = max;
		return this;
	}

	public Double getMax() {
		return max;
	}

}
