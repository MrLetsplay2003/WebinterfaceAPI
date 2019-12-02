package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class StringListSetting implements WebinterfaceSetting<List<String>> {

	private String key;
	private List<String> defaultValue;
	
	public StringListSetting(String key, List<String> defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setDefaultValue(List<String> value) {
		this.defaultValue = value;
	}

	@Override
	public List<String> getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Complex<List<String>> getType() {
		return Complex.list(String.class);
	}

}
