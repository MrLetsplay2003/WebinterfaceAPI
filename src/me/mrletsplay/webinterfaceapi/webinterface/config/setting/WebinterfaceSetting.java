package me.mrletsplay.webinterfaceapi.webinterface.config.setting;

public interface WebinterfaceSetting<T> {
	
	public String getKey();
	
	public T getDefaultValue();
	
}
