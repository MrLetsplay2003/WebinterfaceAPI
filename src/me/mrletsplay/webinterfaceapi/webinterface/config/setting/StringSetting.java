package me.mrletsplay.webinterfaceapi.webinterface.config.setting;

public class StringSetting implements WebinterfaceSetting<String> {
	
	private String key, defaultValue;

	public StringSetting(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public String getKey() {
		return key;
	}
	
	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

}
