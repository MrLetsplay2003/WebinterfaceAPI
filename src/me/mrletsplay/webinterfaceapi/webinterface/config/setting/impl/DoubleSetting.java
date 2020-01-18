package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class DoubleSetting extends SimpleSetting<Double> {

	public DoubleSetting(String key, double defaultValue) {
		super(key, defaultValue, Complex.value(Double.class));
	}
	
	public DoubleSetting(String key, double defaultValue, String friendlyName) {
		super(key, defaultValue, Complex.value(Double.class), friendlyName);
	}

}
