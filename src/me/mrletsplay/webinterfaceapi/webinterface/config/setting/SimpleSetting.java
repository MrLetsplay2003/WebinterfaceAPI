package me.mrletsplay.webinterfaceapi.webinterface.config.setting;

public class SimpleSetting<T> implements WebinterfaceSetting<T> {

	private String key;
	private T defaultValue;
	
	public SimpleSetting(String key, T defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public String getKey() {
		return key;
	}

	@Override
	public T getDefaultValue() {
		return defaultValue;
	}
	
}
