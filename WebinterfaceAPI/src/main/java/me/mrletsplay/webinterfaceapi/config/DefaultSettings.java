package me.mrletsplay.webinterfaceapi.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import me.mrletsplay.simplehttpserver.http.HttpRequestMethod;
import me.mrletsplay.webinterfaceapi.config.setting.AutoSetting;
import me.mrletsplay.webinterfaceapi.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.config.setting.impl.BooleanSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.IntSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.StringListSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.StringSetting;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class DefaultSettings implements AutoSettings {

	public static final DefaultSettings INSTANCE = new DefaultSettings();

	@AutoSetting
	private static SettingsCategory
		general = new SettingsCategory("General"),
		sql = new SettingsCategory("SQL"),
		http = new SettingsCategory("HTTP"),
		https = new SettingsCategory("HTTPS"),
		auth = new SettingsCategory("Auth"),
		php = new SettingsCategory("PHP"),
		performance = new SettingsCategory("Performance"),
		cors = new SettingsCategory("CORS");

	// General

	public static final BooleanSetting
		ENABLE_DEBUG_MODE = general.addBoolean("debug.enable", false, "Enable debug mode");

	public static final StringSetting
		THEME = general.addString("theme", "dark", "Theme"),
		HOME_PAGE_PATH = general.addString("home-page.path", "/wiapi/welcome", "Home page path"),
		ICON_IMAGE = general.addString("icon-image", "icon.svg", "Icon Image", "Path of the icon image inside the include/img resource directory"),
		HEADER_IMAGE = general.addString("header-image", "header.svg", "Header Image", "Path of the header image inside the include/img resource directory"),
		DATABASE = general.addString("database", "file", "Database (requires restart)").allowedValues(Arrays.asList("file", "sql"));

	// SQL

	public static final StringSetting
		SQL_PROVIDER = sql.addString("sql.provider", "mysql", "SQL Provider").allowedValues(Arrays.asList("mysql", "mariadb")),
		SQL_HOST = sql.addString("sql.host", "localhost", "SQL Host");

	public static final IntSetting
		SQL_PORT = sql.addInt("sql.port", 3306, "SQL Port").min(1).max(65535);

	public static final StringSetting
		SQL_USER = sql.addString("sql.user", "webinterfaceapi", "SQL User"),
		SQL_PASSWORD = sql.addString("sql.password", "password", "SQL Password").password(true),
		SQL_DATABASE = sql.addString("sql.database", "webinterfaceapi", "SQL Database"),
		SQL_TABLE_PREFIX = sql.addString("sql.table-prefix", null, "SQL Table Prefix");

	// HTTP

	public static final StringSetting
		HTTP_BASE_URL = http.addString("server.http.base-url", "http://localhost:8880", "HTTP Base URL", "Public URL of the website, absolute URLs will be relative to this. Make sure to not include a trailing slash");

	public static final StringSetting
		HTTP_BIND = http.addString("server.http.bind", "0.0.0.0", "HTTP IP Bind");

	public static final IntSetting
		HTTP_PORT = http.addInt("server.http.port", 8880, "HTTP Port").min(1).max(65535);

	// HTTPS

	public static final BooleanSetting
		HTTPS_ENABLE = https.addBoolean("server.https.enable", false, "Enable HTTPS");

	public static final StringSetting
		HTTPS_BIND = https.addString("server.https.bind", "0.0.0.0", "HTTPS IP Bind");

	public static final IntSetting
		HTTPS_PORT = https.addInt("server.https.port", 8881, "HTTPS Port").min(1).max(65535);

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

	public static final StringSetting
		REGISTRATION_MODE = auth.addString("auth.registration-mode", "enable", "Registration mode").allowedValues(Arrays.asList("enable", "secret", "disable")),
		REGISTRATION_SECRET = auth.addString("auth.registration-secret", WebinterfaceUtils.randomID(16), "Registration secret", "Used for the 'secret' registration mode");

	public static final BooleanSetting
		ALLOW_ANONYMOUS = auth.addBoolean("auth.anonymous", true, "Allow anonymous login"),
		ENABLE_DISCORD_AUTH = auth.addBoolean("auth.discord", true, "Enable Discord auth"),
		ENABLE_GITHUB_AUTH = auth.addBoolean("auth.github", true, "Enable GitHub auth"),
		ENABLE_GOOGLE_AUTH = auth.addBoolean("auth.google", true, "Enable Google auth"),
		ENABLE_PASSWORD_AUTH = auth.addBoolean("auth.password", true, "Enable Password auth");

	// Performance

	public static final BooleanSetting
		ENABLE_FILE_CACHING = performance.addBoolean("performance.file-caching", true, "Enable file caching");

	// CORS

	public static final BooleanSetting
		CORS_ALLOW_ALL_ORIGINS = cors.addBoolean("cors.allow-all-origins", false, "Allow all origins");

	public static final StringListSetting
		CORS_ALLOWED_ORIGINS = cors.addStringList("cors.allowed-origins", Collections.emptyList(), "Allowed origins"),
		CORS_ALLOWED_HEADERS = cors.addStringList("cors.allowed-headers", Collections.emptyList(), "Allowed headers"),
		CORS_EXPOSED_HEADERS = cors.addStringList("cors.exposed-headers", Collections.emptyList(), "Exposed headers");

	public static final IntSetting
		CORS_MAX_AGE = cors.addInt("cors.max-age", 5, "Max age");

	public static final BooleanSetting
		CORS_ALLOW_CREDENTIALS = cors.addBoolean("cors.allow-credentials", false, "Allow credentials"),
		CORS_SEND_ALL_ALLOWED_METHODS = cors.addBoolean("cors.send-all-allowed-methods", false, "Send all allowed methods", "Sends all allowed methods that are configured, not just the ones for which documents are registered");

	public static final StringListSetting
		CORS_ALLOWED_METHODS = cors.addStringList("cors.allowed-methods", Arrays.stream(HttpRequestMethod.values()).map(m -> m.name()).collect(Collectors.toList()), "Allowed methods");

	private DefaultSettings() {}

}
