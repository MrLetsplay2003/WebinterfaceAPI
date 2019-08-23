package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class DoubleListSetting implements WebinterfaceSetting<List<Double>> {

	private String key;
	private List<Double> defaultValue;
	
	public DoubleListSetting(String key, List<Double> defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public List<Double> getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Complex<List<Double>> getType() {
		return Complex.list(Double.class);
	}

}
