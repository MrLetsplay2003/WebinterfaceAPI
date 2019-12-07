package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class StringSetting implements WebinterfaceSetting<String> {

	private String
		key,
		defaultValue;
	
	public StringSetting(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Complex<String> getType() {
		return Complex.value(String.class);
	}

}
