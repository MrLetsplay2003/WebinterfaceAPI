package me.mrletsplay.webinterfaceapi.webinterface.config;

import java.io.File;
import java.util.List;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class WebinterfaceFileConfig implements WebinterfaceConfig {
	
	private File file;
	private FileCustomConfig config;
	
	public WebinterfaceFileConfig(File file) {
		this.file = file;
		this.config = ConfigLoader.loadFileConfig(file);
	}
	
	public File getFile() {
		return file;
	}

	@Override
	public boolean getBooleanSetting(WebinterfaceSetting<Boolean> setting) {
		boolean v = config.getBoolean(setting.getKey(), setting.getDefaultValue(), true);
		config.saveToFile();
		return v;
	}

	@Override
	public String getStringSetting(WebinterfaceSetting<String> setting) {
		String v = config.getString(setting.getKey(), setting.getDefaultValue(), true);
		config.saveToFile();
		return v;
	}

	@Override
	public int getIntSetting(WebinterfaceSetting<Integer> setting) {
		int v = config.getInt(setting.getKey(), setting.getDefaultValue(), true);
		config.saveToFile();
		return v;
	}

	@Override
	public double getDoubleSetting(WebinterfaceSetting<Double> setting) {
		double v = config.getDouble(setting.getKey(), setting.getDefaultValue(), true);
		config.saveToFile();
		return v;
	}

	@Override
	public List<String> getStringListSetting(WebinterfaceSetting<List<String>> setting) {
		List<String> v = config.getStringList(setting.getKey(), setting.getDefaultValue(), true);
		config.saveToFile();
		return v;
	}

	@Override
	public List<Boolean> getBooleanListSetting(WebinterfaceSetting<List<Boolean>> setting) {
		List<Boolean> v = config.getBooleanList(setting.getKey(), setting.getDefaultValue(), true);
		config.saveToFile();
		return v;
	}

	@Override
	public List<Integer> getIntListSetting(WebinterfaceSetting<List<Integer>> setting) {
		List<Integer> v = config.getIntegerList(setting.getKey(), setting.getDefaultValue(), true);
		config.saveToFile();
		return v;
	}

	@Override
	public List<Double> getDoubleListSetting(WebinterfaceSetting<List<Double>> setting) {
		List<Double> v = config.getDoubleList(setting.getKey(), setting.getDefaultValue(), true);
		config.saveToFile();
		return v;
	}

	@Override
	public <T> void setSetting(WebinterfaceSetting<T> setting, T value) {
		config.set(setting.getKey(), setting.getDefaultValue());
		config.saveToFile();
	}
	
	@Override
	public void initializeSetting(WebinterfaceSetting<?> setting) {
		if(!config.isSet(setting.getKey())) {
			config.set(setting.getKey(), setting.getDefaultValue());
			config.saveToFile();
		}
	}

}
