package me.mrletsplay.webinterfaceapi.webinterface.config;

import java.util.Arrays;

import me.mrletsplay.webinterfaceapi.webinterface.config.setting.AutoSetting;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl.BooleanSetting;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl.IntSetting;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl.StringListSetting;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl.StringSetting;

public class DefaultSettings implements AutoSettings {
	
	public static final DefaultSettings INSTANCE = new DefaultSettings();
	
	private DefaultSettings() {}
	
	@AutoSetting
	public static final BooleanSetting
		ENABLE_FILE_CACHING = new BooleanSetting("enable-file-caching", true),
		ENABLE_PHP = new BooleanSetting("php.enable", false);
	
	@AutoSetting
	public static final StringSetting
		THEME = new StringSetting("theme", "blue"),
		PHP_CGI_PATH = new StringSetting("php.cgi-path", "php-cgi");
	
	@AutoSetting
	public static final IntSetting
		PORT = new IntSetting("port", 8880);

	@AutoSetting
	public static final StringListSetting
		INDEX_FILES = new StringListSetting("index-files", Arrays.asList("index.php", "index.html")),
		PHP_FILE_EXTENSIONS = new StringListSetting("php.file-extensions", Arrays.asList(".php"));
	
}