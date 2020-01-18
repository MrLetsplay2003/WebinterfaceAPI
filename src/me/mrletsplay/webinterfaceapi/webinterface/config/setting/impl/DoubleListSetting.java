package me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class DoubleListSetting extends SimpleSetting<List<Double>> {

	public DoubleListSetting(String key, List<Double> defaultValue) {
		super(key, defaultValue, Complex.list(Double.class));
	}

	public DoubleListSetting(String key, List<Double> defaultValue, String friendlyName) {
		super(key, defaultValue, Complex.list(Double.class), friendlyName);
	}

}
