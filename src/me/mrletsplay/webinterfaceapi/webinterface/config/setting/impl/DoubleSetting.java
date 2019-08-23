package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class DoubleSetting implements WebinterfaceSetting<Double> {

	private String key;
	private double defaultValue;
	
	public DoubleSetting(String key, double defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Double getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Complex<Double> getType() {
		return Complex.value(Double.class);
	}

}
