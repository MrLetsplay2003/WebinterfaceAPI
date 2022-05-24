package me.mrletsplay.webinterfaceapi.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class StringListSetting extends AbstractSetting<StringListSetting, List<String>> {

	public StringListSetting(SettingsCategory category, String key, List<String> defaultValue) {
		super(category, key, defaultValue, Complex.list(String.class));
	}

}
