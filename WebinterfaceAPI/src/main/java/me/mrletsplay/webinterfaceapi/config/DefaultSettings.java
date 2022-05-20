package me.mrletsplay.webinterfaceapi.config;

import java.util.Arrays;

import me.mrletsplay.webinterfaceapi.config.setting.AutoSetting;
import me.mrletsplay.webinterfaceapi.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.config.setting.impl.BooleanSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.IntSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.StringListSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.StringSetting;

public class DefaultSettings implements AutoSettings {

	public static final DefaultSettings INSTANCE = new DefaultSettings();

	@AutoSetting
	private static SettingsCategory
		general = new SettingsCategory("General"),
		http = new SettingsCategory("HTTP"),
		https = new SettingsCategory("HTTPS"),
		auth = new SettingsCategory("Auth"),
		php = new SettingsCategory("PHP"),
		performance = new SettingsCategory("Performance");

	// General

	public static final BooleanSetting
		ENABLE_INITIAL_SETUP = general.addBoolean("initial-setup.enable", true, "Enable initial setup"),
		ENABLE_DEBUG_MODE = general.addBoolean("debug.enable", false, "Enable debug mode");

	public static final StringSetting
		THEME = general.addString("theme", "dark", "Theme"),
		HOME_PAGE_PATH = general.addString("home-page.path", "/wiapi/welcome", "Home page path"),
		ICON_IMAGE = general.addString("icon-image", "/_internal/img/icon.svg", "Icon Image"),
		HEADER_IMAGE = general.addString("header-image", "/_internal/img/header.svg", "Header Image");

	public static final StringListSetting
		INDEX_FILES = general.addStringList("index-files", Arrays.asList("index.php", "index.html"), "Index file names");


	// HTTP

	public static final BooleanSetting
		USE_CLIENT_HOST = http.addBoolean("server.http.use-client-host", false, "Use client HTTP Host");

	public static final StringSetting
		HTTP_BIND = http.addString("server.http.bind", "0.0.0.0", "HTTP IP Bind"),
		HTTP_HOST = http.addString("server.http.host", "localhost", "HTTP Host");

	public static final IntSetting
		HTTP_PORT = http.addInt("server.http.port", 8880, "HTTP Port");


	// HTTPS

	public static final BooleanSetting
		HTTPS_ENABLE = https.addBoolean("server.https.enable", false, "Enable HTTPS");

	public static final StringSetting
		HTTPS_BIND = https.addString("server.https.bind", "0.0.0.0", "HTTPS IP Bind"),
		HTTPS_HOST = https.addString("server.https.host", "localhost", "HTTPS Host");

	public static final IntSetting
		HTTPS_PORT = https.addInt("server.https.port", 8881, "HTTPS Port");

	public static final StringSetting
		HTTPS_CERTIFICATE_PATH = https.addString("server.https.certificate.path", null, "HTTPS Certificate path"),
		HTTPS_CERTIFICATE_PASSWORD = https.addString("server.https.certificate.password", null, "HTTPS Certificate password"),
		HTTPS_CERTIFICATE_KEY_PATH = https.addString("server.https.certificate.key-path", null, "HTTPS Certificate Key path");


	// PHP

	public static final BooleanSetting
		ENABLE_PHP = php.addBoolean("php.enable", false, "Enable PHP");

	public static final StringSetting
		PHP_CGI_PATH = php.addString("php.cgi-path", "php-cgi", "PHP CGI path");

	public static final StringListSetting
		PHP_FILE_EXTENSIONS = php.addStringList("php.file-extensions", Arrays.asList(".php"), "PHP file extensions");


	//Auth

	public static final BooleanSetting
		ALLOW_REGISTRATION = auth.addBoolean("allow-registration", true, "Allow registration"),
		ALLOW_ANONYMOUS = auth.addBoolean("allow-anonymous", true, "Allow anonymous login"),
		ENABLE_DISCORD_AUTH = auth.addBoolean("use-auth.discord", true, "Enable Discord auth"),
		ENABLE_GITHUB_AUTH = auth.addBoolean("use-auth.github", true, "Enable GitHub auth"),
		ENABLE_GOOGLE_AUTH = auth.addBoolean("use-auth.google", true, "Enable Google auth"),
		ENABLE_PASSWORD_AUTH = auth.addBoolean("use-auth.password", true, "Enable Password auth");


	// Performance

	public static final BooleanSetting
		ENABLE_FILE_CACHING = performance.addBoolean("enable-file-caching", true, "Enable file caching");

	private DefaultSettings() {}

}
