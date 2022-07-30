package me.mrletsplay.webinterfaceapi.auth.impl;

import java.io.File;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.http.HttpGet;
import me.mrletsplay.mrcore.http.HttpPost;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.http.data.URLEncodedData;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.header.HttpURLPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.auth.AuthException;
import me.mrletsplay.webinterfaceapi.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class GitHubAuth implements AuthMethod {

	public static final String
		ID = "github";

	private static final String
		AUTH_ENDPOINT = "https://github.com/login/oauth/authorize",
		TOKEN_ENDPOINT = "https://github.com/login/oauth/access_token",
		USERINFO_ENDPOINT = "https://api.github.com/user",
		USER_EMAILS_ENDPOINT = "https://api.github.com/user/emails";

	private boolean configured;

	private String
		clientID,
		clientSecret;

	@Override
	public void initialize() {
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
		if(Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_GITHUB_AUTH) && !configured) Webinterface.getLogger().warn("GitHub auth needs to be configured");
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
		return "GitHub";
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);

		URLEncoded query = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery();
		boolean connect = query.has("connect") && query.getFirst("connect").equals("true");

		c.getServerHeader().getFields().set("Location", AUTH_ENDPOINT
				+ "?client_id=" + HttpUtils.urlEncode(clientID)
				+ "&redirect_uri=" + HttpUtils.urlEncode(getAuthResponseUrl().toString())
				+ "&response_type=code"
				+ "&scope=" + HttpUtils.urlEncode("read:user user:email")
				+ "&state=" + (connect ? "connect~" : "") + HttpUtils.urlEncode(HttpRequestContext.getCurrentContext().getRequestedPath().getQuery().getFirst("from", "/")));
	}

	@Override
	public AccountConnection handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		String code = c.getRequestedPath().getQuery().getFirst("code");
		try {
			HttpPost p = HttpRequest.createPost(TOKEN_ENDPOINT)
				.setHeader("Accept", "application/json")
				.setData(new URLEncodedData()
					.set("code", code)
					.set("client_id", clientID)
					.set("client_secret", clientSecret)
					.set("redirect_uri", getAuthResponseUrl().toString())
					.set("grant_type", "authorization_code"));

			JSONObject res = p.execute().asJSONObject();

			String accToken = res.getString("access_token");

			HttpGet g = HttpRequest.createGet(USERINFO_ENDPOINT)
					.setHeader("Accept", "application/json")
					.setHeader("Authorization", "token " + accToken);
			JSONObject usr = g.execute().asJSONObject();

			String
				userID = String.valueOf(usr.getLong("id")),
				userName = usr.getString("name"),
				userEmail,
				userAvatarUrl = usr.getString("avatar_url");

			// Use primary email, not public email
			HttpGet g2 = HttpRequest.createGet(USER_EMAILS_ENDPOINT)
					.setHeader("Accept", "application/json")
					.setHeader("Authorization", "token " + accToken);
			JSONArray emls = g2.execute().asJSONArray();
			JSONObject primEml = emls.stream().map(o -> (JSONObject) o).filter(e -> e.getBoolean("primary")).findFirst().orElse(null);
			userEmail = primEml.getString("email");

			return new AccountConnection(getID(), userID, userName, userEmail, userAvatarUrl);
		} catch (Exception e) {
			throw new AuthException("Failed to verify GitHub auth token", e);
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
		return configured && Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_GITHUB_AUTH);
	}

}
