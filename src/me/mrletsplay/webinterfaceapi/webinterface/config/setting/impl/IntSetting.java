package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class IntSetting extends SimpleSetting<Integer> {

	public IntSetting(String key, int defaultValue) {
		super(key, defaultValue, Complex.value(Integer.class));
	}
	
	public IntSetting(String key, int defaultValue, String friendlyName) {
		super(key, defaultValue, Complex.value(Integer.class), friendlyName);
	}

}
