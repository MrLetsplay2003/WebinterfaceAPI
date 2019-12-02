package me.mrletsplay.webinterfaceapi.webinterface.config.setting;

import me.mrletsplay.mrcore.misc.Complex;

public class SimpleSetting<T> implements WebinterfaceSetting<T> {

	private String key;
	private T defaultValue;
	private Complex<T> type;
	
	public SimpleSetting(String key, T defaultValue, Complex<T> type) {
		this.key = key;
		this.defaultValue = defaultValue;
		this.type = type;
	}
	
	@Override
	public String getKey() {
		return key;
	}
	
	@Override
	public void setDefaultValue(T value) {
		this.defaultValue = value;
	}

	@Override
	public T getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public Complex<T> getType() {
		return type;
	}
	
}
