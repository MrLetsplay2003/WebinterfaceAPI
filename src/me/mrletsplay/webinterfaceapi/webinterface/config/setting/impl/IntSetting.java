package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class IntSetting implements WebinterfaceSetting<Integer> {

	private String key;
	private int defaultValue;
	
	public IntSetting(String key, int defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Integer getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Complex<Integer> getType() {
		return Complex.value(Integer.class);
	}

}
