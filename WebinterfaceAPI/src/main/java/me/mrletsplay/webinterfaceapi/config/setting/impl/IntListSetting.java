package me.mrletsplay.webinterfaceapi.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class IntListSetting extends AbstractSetting<IntListSetting, List<Integer>> {

	public IntListSetting(SettingsCategory category, String key, List<Integer> defaultValue) {
		super(category, key, defaultValue, Complex.list(Integer.class));
	}

}
