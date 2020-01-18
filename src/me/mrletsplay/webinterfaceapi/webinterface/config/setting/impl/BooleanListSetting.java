package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class BooleanListSetting extends SimpleSetting<List<Boolean>> {

	public BooleanListSetting(String key, List<Boolean> defaultValue) {
		super(key, defaultValue, Complex.list(Boolean.class));
	}

	public BooleanListSetting(String key, List<Boolean> defaultValue, String friendlyName) {
		super(key, defaultValue, Complex.list(Boolean.class), friendlyName);
	}
	
}
