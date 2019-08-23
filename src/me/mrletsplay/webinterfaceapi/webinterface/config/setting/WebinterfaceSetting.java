package me.mrletsplay.webinterfaceapi.webinterface.config.setting;

import me.mrletsplay.mrcore.misc.Complex;

public interface WebinterfaceSetting<T> {
	
	public String getKey();
	
	public T getDefaultValue();
	
	public Complex<T> getType();
	
}
