package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class IntListSetting implements WebinterfaceSetting<List<Integer>> {

	private String key;
	private List<Integer> defaultValue;
	
	public IntListSetting(String key, List<Integer> defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setDefaultValue(List<Integer> value) {
		this.defaultValue = value;
	}

	@Override
	public List<Integer> getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Complex<List<Integer>> getType() {
		return Complex.list(Integer.class);
	}

}
