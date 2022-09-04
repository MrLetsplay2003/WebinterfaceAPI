package me.mrletsplay.webinterfaceapi.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.config.setting.AbstractSetting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;

public class StringSetting extends AbstractSetting<StringSetting, String> {

	private boolean password;

	public StringSetting(SettingsCategory category, String key, String defaultValue) {
		super(category, key, defaultValue, Complex.value(String.class));
	}

	public StringSetting password(boolean password) {
		this.password = password;
		return this;
	}

	public boolean isPassword() {
		return password;
	}

}
