package me.mrletsplay.webinterfaceapi.config.setting;

import me.mrletsplay.mrcore.misc.Complex;

public interface Setting<T> {
	
	public String getKey();
	
	public void setDefaultValue(T value);
	
	public T getDefaultValue();
	
	public Complex<T> getType();
	
	public String getFriendlyName();
	
	public String getDescription();
	
	public SettingsCategory getCategory();
	
}
