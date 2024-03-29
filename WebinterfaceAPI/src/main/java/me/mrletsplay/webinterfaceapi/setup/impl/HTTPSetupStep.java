package me.mrletsplay.webinterfaceapi.setup.impl;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.setup.SetupStep;

public class HTTPSetupStep extends SetupStep {

	public HTTPSetupStep() {
		super("http", "Set up the web server");
		setDescription("Configure your server so it works as smoothly as possible");

		addHeading("HTTP");
		addString("http-base-url", "HTTP Base URL", DefaultSettings.HTTP_BASE_URL.getDefaultValue());
		addString("http-bind", "HTTP IP Bind", DefaultSettings.HTTP_BIND.getDefaultValue());
		addInteger("http-port", "HTTP Port", DefaultSettings.HTTP_PORT.getDefaultValue());

		addHeading("HTTPS");
		addBoolean("enable-https", "Enable HTTPS", false);
		addString("https-bind", "HTTPS IP Bind", DefaultSettings.HTTPS_BIND.getDefaultValue());
		addInteger("https-port", "HTTPS Port", DefaultSettings.HTTPS_PORT.getDefaultValue());
		addString("https-cert-path", "HTTPS Certificate Path", null);
		addPassword("https-cert-password", "HTTPS Certificate Password (leave empty for none)", null);
		addString("https-cert-key-path", "HTTPS Certificate Key Path", null);
	}

	@Override
	public String callback(JSONObject data) {
		String httpBaseURL = data.getString("http-base-url").trim();
		String httpBind = data.getString("http-bind").trim();
		int httpPort = data.getInt("http-port");

		if(httpBaseURL.isEmpty()) return "HTTP Base URL must be set";
		if(httpBind.isEmpty()) return "HTTP Bind must be set";
		if(httpPort < 1 || httpPort > 65535) return "Invalid HTTP port";

		Config cfg = Webinterface.getConfig();
		cfg.setSetting(DefaultSettings.HTTP_BASE_URL, httpBaseURL);
		cfg.setSetting(DefaultSettings.HTTP_BIND, httpBind);
		cfg.setSetting(DefaultSettings.HTTP_PORT, httpPort);

		boolean enableHttps = data.getBoolean("enable-https");
		if(enableHttps) {
			String httpsBind = data.getString("https-bind").trim();
			int httpsPort = data.getInt("https-port");

			String certPath = data.getString("https-cert-path").trim();
			String certPass = data.getString("https-cert-password").trim();
			String certKeyPath = data.getString("https-cert-key-path");

			if(httpsBind.isEmpty()) return "HTTPS Bind must be set";
			if(httpsPort < 1 || httpsPort > 65535) return "Invalid HTTPS port";

			if(certPath.isEmpty()) return "HTTPS certificate path must be set";
			if(certKeyPath.isEmpty()) return "HTTPS certificate key path must be set";

			cfg.setSetting(DefaultSettings.HTTPS_BIND, httpsBind);
			cfg.setSetting(DefaultSettings.HTTPS_PORT, httpsPort);

			cfg.setSetting(DefaultSettings.HTTPS_CERTIFICATE_PATH, certPath);
			cfg.setSetting(DefaultSettings.HTTPS_CERTIFICATE_PASSWORD, certPass);
			cfg.setSetting(DefaultSettings.HTTPS_CERTIFICATE_KEY_PATH, certKeyPath);
		}

		return null;
	}

}
