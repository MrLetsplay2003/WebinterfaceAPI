package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class IntListSetting extends SimpleSetting<List<Integer>> {

	public IntListSetting(SettingsCategory category, String key, List<Integer> defaultValue) {
		super(category, key, defaultValue, Complex.list(Integer.class));
	}
	
}
