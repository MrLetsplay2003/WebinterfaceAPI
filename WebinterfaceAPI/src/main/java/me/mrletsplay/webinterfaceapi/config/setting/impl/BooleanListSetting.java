package me.mrletsplay.webinterfaceapi.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class BooleanListSetting extends AbstractSetting<BooleanListSetting, List<Boolean>> {

	public BooleanListSetting(SettingsCategory category, String key, List<Boolean> defaultValue) {
		super(category, key, defaultValue, Complex.list(Boolean.class));
	}

}
