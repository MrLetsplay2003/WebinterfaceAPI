package me.mrletsplay.webinterfaceapi.webinterface.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;

public class WebinterfaceFileConfig implements WebinterfaceConfig {
	
	private File file;
	private FileCustomConfig config;
	private List<WebinterfaceSetting<?>> settings;
	
	public WebinterfaceFileConfig(File file) {
		this.file = file;
		this.config = ConfigLoader.loadFileConfig(file);
		this.settings = new ArrayList<>();
	}
	
	public File getFile() {
		return file;
	}

	@Override
	public <T> T getSetting(WebinterfaceSetting<T> setting) {
		T val = config.getComplex(setting.getKey(), setting.getType(), setting.getDefaultValue(), true);
		config.saveToFile();
		return val;
	}

	@Override
	public <T> void setSetting(WebinterfaceSetting<T> setting, T value) {
		config.set(setting.getKey(), value);
		config.saveToFile();
	}
	
	@Override
	public void registerSetting(WebinterfaceSetting<?> setting) {
		settings.add(setting);
		if(!config.isSet(setting.getKey())) {
			config.set(setting.getKey(), setting.getDefaultValue());
			config.saveToFile();
		}
	}
	
	@Override
	public List<WebinterfaceSetting<?>> getSettings() {
		return settings;
	}
	
	@Override
	public <T> T getOverride(String overrideTarget, Class<T> type) {
		return config.getGeneric("override." + overrideTarget, type);
	}
	
	@Override
	public void setOverride(String overrideTarget, Object override) {
		config.set("override." + overrideTarget, override);
		config.saveToFile();
	}

}
