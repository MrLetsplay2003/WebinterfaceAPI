package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class BooleanSetting implements WebinterfaceSetting<Boolean> {

	private String key;
	private boolean defaultValue;
	
	public BooleanSetting(String key, boolean defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}
	
	@Override
	public void setDefaultValue(Boolean value) {
		this.defaultValue = value;
	}
	
	@Override
	public Boolean getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Complex<Boolean> getType() {
		return Complex.value(Boolean.class);
	}

}
