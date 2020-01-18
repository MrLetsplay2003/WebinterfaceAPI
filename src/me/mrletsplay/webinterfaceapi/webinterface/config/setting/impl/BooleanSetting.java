package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class BooleanSetting extends SimpleSetting<Boolean> {

	public BooleanSetting(String key, boolean defaultValue) {
		super(key, defaultValue, Complex.value(Boolean.class));
	}

	public BooleanSetting(String key, boolean defaultValue, String friendlyName) {
		super(key, defaultValue, Complex.value(Boolean.class), friendlyName);
	}

}
