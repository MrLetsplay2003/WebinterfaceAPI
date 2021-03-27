package me.mrletsplay.webinterfaceapi.webinterface.config.setting;

import me.mrletsplay.mrcore.misc.Complex;

public class SimpleSetting<T> implements WebinterfaceSetting<T> {

	private SettingsCategory category;
	private String key;
	private T defaultValue;
	private Complex<T> type;
	private String friendlyName;
	
	public SimpleSetting(SettingsCategory category, String key, T defaultValue, Complex<T> type) {
		this.category = category;
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
	
	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	@Override
	public String getFriendlyName() {
		return friendlyName;
	}
	
	@Override
	public SettingsCategory getCategory() {
		return category;
	}
	
}
