package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import java.io.File;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.http.HttpGet;
import me.mrletsplay.mrcore.http.HttpPost;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.header.HttpURLPath;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class GitHubAuth implements WebinterfaceAuthMethod {

	private static final String
		AUTH_ENDPOINT = "https://github.com/login/oauth/authorize",
		TOKEN_ENDPOINT = "https://github.com/login/oauth/access_token",
		USERINFO_ENDPOINT = "https://api.github.com/user",
		USER_EMAILS_ENDPOINT = "https://api.github.com/user/emails";
	
	private boolean available;
	
	private String
		clientID,
		clientSecret;
	
	public GitHubAuth() {
		File cfgFile = new File(getConfigurationDirectory(), "credentials.yml");
		FileCustomConfig cfg = ConfigLoader.loadFileConfig(cfgFile);
		try {
			clientID = cfg.getString("client-id", null, true);
			clientSecret = cfg.getString("client-secret", null, true);
			cfg.saveToFile();
			if(clientID != null && clientSecret != null) available = true;
		} catch (Exception e) {
			available = false;
		}
		if(!available) Webinterface.getLogger().warning("GitHub auth needs to be configured");
	}
	
	@Override
	public String getID() {
		return "github";
	}

	@Override
	public String getName() {
		return "GitHub";
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);

		HttpURLPath clientPath = HttpRequestContext.getCurrentContext().getClientHeader().getPath();
		boolean connect = clientPath.hasQueryParameter("connect") && clientPath.getQueryParameterValue("connect").equals("true");
		
		c.getServerHeader().getFields().setFieldValue("Location", AUTH_ENDPOINT
				+ "?client_id=" + HttpUtils.urlEncode(clientID)
				+ "&redirect_uri=" + HttpUtils.urlEncode(getAuthResponseUrl().toString())
				+ "&response_type=code"
				+ "&scope=" + HttpUtils.urlEncode("read:user user:email")
				+ "&state=" + (connect ? "connect~" : "") + HttpUtils.urlEncode(HttpRequestContext.getCurrentContext().getClientHeader().getPath().getQueryParameterValue("from", "/")));
	}

	@Override
	public WebinterfaceAccountConnection handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		String code = c.getClientHeader().getPath().getQueryParameterValue("code");
		try {
			HttpPost p = HttpRequest.createPost(TOKEN_ENDPOINT)
					.setHeaderParameter("Accept", "application/json")
					.setPostParameter("code", code)
					.setPostParameter("client_id", clientID)
					.setPostParameter("client_secret", clientSecret)
					.setPostParameter("redirect_uri", getAuthResponseUrl().toString())
					.setPostParameter("grant_type", "authorization_code");
			JSONObject res = p.execute().asJSONObject();
			
			String accToken = res.getString("access_token");
			
			HttpGet g = HttpRequest.createGet(USERINFO_ENDPOINT)
					.setHeaderParameter("Accept", "application/json")
					.setHeaderParameter("Authorization", "token " + accToken);
			JSONObject usr = g.execute().asJSONObject();
			
			String
				userID = String.valueOf(usr.getLong("id")),
				userName = usr.getString("name"),
				userEmail,
				userAvatarUrl = usr.getString("avatar_url");
			
			// Use primary email, not public email
			HttpGet g2 = HttpRequest.createGet(USER_EMAILS_ENDPOINT)
					.setHeaderParameter("Accept", "application/json")
					.setHeaderParameter("Authorization", "token " + accToken);
			JSONArray emls = g2.execute().asJSONArray();
			JSONObject primEml = emls.stream().map(o -> (JSONObject) o).filter(e -> e.getBoolean("primary")).findFirst().orElse(null);
			userEmail = primEml.getString("email");
			
			return new WebinterfaceAccountConnection(getID(), userID, userName, userEmail, userAvatarUrl);
		} catch (Exception e) {
			throw new AuthException("Failed to verify GitHub auth token", e);
		}
	}
	
	@Override
	public HttpURLPath getPostAuthRedirectURL() {
		String path = HttpRequestContext.getCurrentContext().getClientHeader().getPath().getQueryParameterValue("state", "/");
		if(path.startsWith("connect~")) path = path.substring("connect~".length());
		return HttpURLPath.of(path);
	}
	
	@Override
	public boolean getShouldConnect() {
		String path = HttpRequestContext.getCurrentContext().getClientHeader().getPath().getQueryParameterValue("state", "/");
		return path.startsWith("connect~");
	}
	
	@Override
	public boolean isAvailable() {
		return available;
	}

}
