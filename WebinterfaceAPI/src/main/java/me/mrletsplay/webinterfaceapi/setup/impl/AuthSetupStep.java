package me.mrletsplay.webinterfaceapi.setup.impl;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.impl.DiscordAuth;
import me.mrletsplay.webinterfaceapi.auth.impl.GitHubAuth;
import me.mrletsplay.webinterfaceapi.auth.impl.GoogleAuth;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.setup.ChoiceList;
import me.mrletsplay.webinterfaceapi.setup.SetupStep;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class AuthSetupStep extends SetupStep {

	public AuthSetupStep() {
		super("auth", "Configure authentication methods");
		setDescription("Set up authentication methods for people to log in with");

		addHeading("Registration");
		addChoice("registration-mode", "Registration mode", new ChoiceList()
			.addChoice("enable", "Allow all registration")
			.addChoice("secret", "Require registration secret")
			.addChoice("disable", "Disable registration"),
			"enable");

		addString("registration-secret", "Registration secret", WebinterfaceUtils.randomID(16));

		addHeading("Anonymous");
		addBoolean("no-auth", "Allow anonymous login", true);

		addHeading("Discord");
		addBoolean("discord-auth", "Enable Discord auth", false);
		addString("discord-client-id", "Discord client ID", null);
		addPassword("discord-client-secret", "Discord client secret", null);

		addHeading("Google");
		addBoolean("google-auth", "Enable Google auth", false);
		addString("google-client-id", "Google client ID", null);
		addPassword("google-client-secret", "Google client secret", null);

		addHeading("GitHub");
		addBoolean("github-auth", "Enable GitHub auth", false);
		addString("github-client-id", "GitHub client ID", null);
		addPassword("github-client-secret", "GitHub client secret", null);
	}

	@Override
	public String callback(JSONObject data) {
		Config cfg = Webinterface.getConfig();

		String registrationMode = data.getString("registration-mode");
		cfg.setSetting(DefaultSettings.REGISTRATION_MODE, registrationMode);

		String registrationSecret = data.getString("registration-secret").trim();
		if("secret".equals(registrationMode) && registrationSecret.isEmpty()) return "Registration secret must be set when using 'secret' registration mode";
		if(!registrationSecret.isEmpty()) cfg.setSetting(DefaultSettings.REGISTRATION_SECRET, registrationSecret);

		boolean noAuth = data.getBoolean("no-auth");
		cfg.setSetting(DefaultSettings.ALLOW_ANONYMOUS, noAuth);

		boolean discordAuth = data.getBoolean("discord-auth");
		cfg.setSetting(DefaultSettings.ENABLE_DISCORD_AUTH, discordAuth);
		if(discordAuth) {
			String clientID = data.getString("discord-client-id").trim();
			String clientSecret = data.getString("discord-client-secret").trim();

			if(clientID.isEmpty()) return "Discord Client ID must be set";
			if(clientSecret.isEmpty()) return "Discord Client Secret must be set";

			DiscordAuth a = (DiscordAuth) Webinterface.getAuthMethodByID(DiscordAuth.ID);
			a.setup(clientID, clientSecret);
		}

		boolean googleAuth = data.getBoolean("google-auth");
		cfg.setSetting(DefaultSettings.ENABLE_GOOGLE_AUTH, googleAuth);
		if(googleAuth) {
			String clientID = data.getString("google-client-id").trim();
			String clientSecret = data.getString("google-client-secret").trim();

			if(clientID.isEmpty()) return "Google Client ID must be set";
			if(clientSecret.isEmpty()) return "Google Client Secret must be set";

			GoogleAuth a = (GoogleAuth) Webinterface.getAuthMethodByID(GoogleAuth.ID);
			a.setup(clientID, clientSecret);
		}

		boolean githubAuth = data.getBoolean("github-auth");
		cfg.setSetting(DefaultSettings.ENABLE_GITHUB_AUTH, githubAuth);
		if(githubAuth) {
			String clientID = data.getString("github-client-id").trim();
			String clientSecret = data.getString("github-client-secret").trim();

			if(clientID.isEmpty()) return "GitHub Client ID must be set";
			if(clientSecret.isEmpty()) return "GitHub Client Secret must be set";

			GitHubAuth a = (GitHubAuth) Webinterface.getAuthMethodByID(GitHubAuth.ID);
			a.setup(clientID, clientSecret);
		}

		return null;
	}

}
