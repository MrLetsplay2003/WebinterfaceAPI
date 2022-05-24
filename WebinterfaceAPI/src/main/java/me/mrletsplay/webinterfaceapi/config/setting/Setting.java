package me.mrletsplay.webinterfaceapi.config.setting;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;

public interface Setting<T> {

	public String getKey();

	public T getDefaultValue();

	public List<T> getAllowedValues();

	public Complex<T> getType();

	public String getFriendlyName();

	public String getDescription();

	public SettingsCategory getCategory();

}
