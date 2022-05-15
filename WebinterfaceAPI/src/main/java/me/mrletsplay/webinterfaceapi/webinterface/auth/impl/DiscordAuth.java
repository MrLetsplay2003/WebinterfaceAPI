package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import java.io.File;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.http.HttpGet;
import me.mrletsplay.mrcore.http.HttpPost;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.http.data.URLEncodedData;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.header.HttpURLPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class DiscordAuth implements WebinterfaceAuthMethod {

	public static final String
		ID = "discord";

	private static final String
		USER_ENDPOINT = "https://discord.com/api/v10/users/@me",
		AUTH_ENDPOINT = "https://discord.com/api/oauth2/authorize",
		TOKEN_ENDPOINT = "https://discord.com/api/oauth2/token",
		USER_AVATAR_URL = "https://cdn.discordapp.com/avatars/%s/%s.%s?size=64",
		DEFAULT_AVATAR_URL = "https://cdn.discordapp.com/embed/avatars/%s.png?size=64";

	private boolean configured;

	private String
		clientID,
		clientSecret;

	public DiscordAuth() {
		File cfgFile = new File(getConfigurationDirectory(), "credentials.yml");
		FileCustomConfig cfg = ConfigLoader.loadFileConfig(cfgFile);
		try {
			clientID = cfg.getString("client-id", null, true);
			clientSecret = cfg.getString("client-secret", null, true);
			cfg.saveToFile();
			if(clientID != null && clientSecret != null) configured = true;
		} catch (Exception e) {
			configured = false;
		}
		if(Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_DISCORD_AUTH) && !configured) Webinterface.getLogger().warn("Discord auth needs to be configured");
	}

	public void setup(String clientID, String clientSecret) {
		this.clientID = clientID;
		this.clientSecret = clientSecret;
		File cfgFile = new File(getConfigurationDirectory(), "credentials.yml");
		FileCustomConfig cfg = ConfigLoader.loadFileConfig(cfgFile);
		cfg.set("client-id", clientID);
		cfg.set("client-secret", clientSecret);
		cfg.saveToFile();
		configured = true;
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String getName() {
		return "Discord";
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);

		URLEncoded query = HttpRequestContext.getCurrentContext().getClientHeader().getPath().getQuery();
		boolean connect = query.has("connect") && query.getFirst("connect").equals("true");

		c.getServerHeader().getFields().set("Location", AUTH_ENDPOINT
				+ "?client_id=" + HttpUtils.urlEncode(clientID)
				+ "&redirect_uri=" + HttpUtils.urlEncode(getAuthResponseUrl().toString()) // TODO: protocol
				+ "&response_type=code"
				+ "&scope=identify email"
				+ "&state=" + (connect ? "connect~" : "") + HttpUtils.urlEncode(query.getFirst("from", "/"))
				+ "&prompt=none");
	}

	@Override
	public WebinterfaceAccountConnection handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		String code = c.getRequestedPath().getQuery().getFirst("code");

		try {
			HttpPost p = HttpRequest.createPost(TOKEN_ENDPOINT)
				.setData(new URLEncodedData()
					.set("client_id", clientID)
					.set("client_secret", clientSecret)
					.set("grant_type", "authorization_code")
					.set("code", code)
					.set("redirect_uri", getAuthResponseUrl().toString())
					.set("scope", "identify email"));
			JSONObject res = p.execute().asJSONObject();
			String accToken = res.getString("access_token");

			HttpGet g = HttpRequest.createGet(USER_ENDPOINT)
					.setHeader("Authorization", "Bearer " + accToken);
			JSONObject usr = g.execute().asJSONObject();

			String
				userID = usr.getString("id"),
				userName = usr.getString("username"),
				userEmail = usr.getString("email"),
				userAvatar = usr.getString("avatar"),
				userAvatarUrl;

			int discriminator = Integer.parseInt(usr.getString("discriminator"));

			if(userAvatar != null) {
				userAvatarUrl = String.format(USER_AVATAR_URL, userID, userAvatar, userAvatar.startsWith("a_") ? "gif" : "png");
			}else {
				userAvatarUrl = String.format(DEFAULT_AVATAR_URL, discriminator % 5);
			}

			return new WebinterfaceAccountConnection(getID(), userID, userName, userEmail, userAvatarUrl);
		} catch (Exception e) {
			throw new AuthException("Failed to verify Discord auth token", e);
		}
	}

	@Override
	public HttpURLPath getPostAuthRedirectURL() {
		String path = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery().getFirst("state", "/");
		if(path.startsWith("connect~")) path = path.substring("connect~".length());
		return HttpURLPath.of(path);
	}

	@Override
	public boolean getShouldConnect() {
		String path = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery().getFirst("state", "/");
		return path.startsWith("connect~");
	}

	@Override
	public boolean isAvailable() {
		return configured && Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_DISCORD_AUTH);
	}

}
