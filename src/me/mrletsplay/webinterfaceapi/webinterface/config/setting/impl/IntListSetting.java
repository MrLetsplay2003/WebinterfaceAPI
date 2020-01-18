package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class IntListSetting extends SimpleSetting<List<Integer>> {

	public IntListSetting(String key, List<Integer> defaultValue) {
		super(key, defaultValue, Complex.list(Integer.class));
	}
	
	public IntListSetting(String key, List<Integer> defaultValue, String friendlyName) {
		super(key, defaultValue, Complex.list(Integer.class), friendlyName);
	}

}
