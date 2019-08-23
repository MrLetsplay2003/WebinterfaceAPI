package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class BooleanListSetting implements WebinterfaceSetting<List<Boolean>> {

	private String key;
	private List<Boolean> defaultValue;
	
	public BooleanListSetting(String key, List<Boolean> defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public List<Boolean> getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Complex<List<Boolean>> getType() {
		return Complex.list(Boolean.class);
	}

}
