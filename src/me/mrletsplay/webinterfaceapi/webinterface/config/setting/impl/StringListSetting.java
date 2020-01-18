package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class StringListSetting extends SimpleSetting<List<String>> {

	public StringListSetting(String key, List<String> defaultValue) {
		super(key, defaultValue, Complex.list(String.class));
	}
	
	public StringListSetting(String key, List<String> defaultValue, String friendlyName) {
		super(key, defaultValue, Complex.list(String.class), friendlyName);
	}

}
