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
	
	@AutoSetting
	public static final BooleanSetting
		ENABLE_FILE_CACHING = new BooleanSetting("enable-file-caching", true, "Enable file caching"),
		ENABLE_PHP = new BooleanSetting("php.enable", false, "Enable PHP"),
		MINIFY_SCRIPTS = new BooleanSetting("minify-scripts", true, "Minify scripts"),
		ALLOW_REGISTRATION = new BooleanSetting("allow-registration", true, "Allow registration"),
		ALLOW_ANONYMOUS = new BooleanSetting("allow-anonymous", true, "Allow anonymous login"),
		ENABLE_DISCORD_AUTH = new BooleanSetting("use-auth.discord", true, "Enable Discord auth"),
		ENABLE_GITHUB_AUTH = new BooleanSetting("use-auth.github", true, "Enable GitHub auth"),
		ENABLE_GOOGLE_AUTH = new BooleanSetting("use-auth.google", true, "Enable Google auth"),
		ENABLE_INITIAL_SETUP = new BooleanSetting("initial-setup.enable", true, "Enable initial setup");
	
	@AutoSetting
	public static final StringSetting
		THEME = new StringSetting("theme", "blue", "Theme"),
		PHP_CGI_PATH = new StringSetting("php.cgi-path", "php-cgi", "PHP CGI path"),
		HOST = new StringSetting("host", "0.0.0.0", "Host");
	
	@AutoSetting
	public static final IntSetting
		PORT = new IntSetting("port", 8880, "Port");

	@AutoSetting
	public static final StringListSetting
		INDEX_FILES = new StringListSetting("index-files", Arrays.asList("index.php", "index.html"), "Index file names"),
		PHP_FILE_EXTENSIONS = new StringListSetting("php.file-extensions", Arrays.asList(".php"), "PHP file extensions");
	
	private DefaultSettings() {}
	
}
