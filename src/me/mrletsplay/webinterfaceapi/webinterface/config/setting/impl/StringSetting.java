package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class StringSetting extends SimpleSetting<String> {

	public StringSetting(String key, String defaultValue) {
		super(key, defaultValue, Complex.value(String.class));
	}
	
	public StringSetting(String key, String defaultValue, String friendlyName) {
		super(key, defaultValue, Complex.value(String.class), friendlyName);
	}

}
