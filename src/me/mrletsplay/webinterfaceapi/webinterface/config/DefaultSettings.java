package me.mrletsplay.webinterfaceapi.webinterface.config;

import java.util.Arrays;
import java.util.List;

import me.mrletsplay.webinterfaceapi.webinterface.config.setting.AutoSetting;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SimpleSetting;

public class DefaultSettings implements AutoSettings {
	
	@AutoSetting
	public static final SimpleSetting<Boolean>
		ENABLE_FILE_CACHING = new SimpleSetting<>("enable-file-caching", true);

	@AutoSetting
	public static final SimpleSetting<String>
		THEME = new SimpleSetting<>("theme", "blue");
	
	@AutoSetting
	public static final SimpleSetting<Integer>
		PORT = new SimpleSetting<>("port", 8880);

	@AutoSetting
	public static final SimpleSetting<List<String>>
		INDEX_FILES = new SimpleSetting<>("index-files", Arrays.asList("index.php", "index.html"));
	
}
