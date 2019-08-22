package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import java.io.File;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.http.HttpGet;
import me.mrletsplay.mrcore.http.HttpPost;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountData;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class DiscordAuth implements WebinterfaceAuthMethod {

	private static final String
		AUTH_ENDPOINT = "https://discordapp.com/api/oauth2/authorize",
		TOKEN_ENDPOINT = "https://discordapp.com/api/oauth2/token";
	
	private boolean available;
	private String clientID, clientSecret;
	
	public DiscordAuth() {
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
		if(!available) System.out.println("[WIAPI] Discord auth needs to be configured");
	}
	
	@Override
	public String getID() {
		return "discord";
	}

	@Override
	public String getName() {
		return "Discord";
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
		c.getServerHeader().getFields().setFieldValue("Location", AUTH_ENDPOINT
				+ "?client_id=" + HttpUtils.urlEncode(clientID)
				+ "&redirect_uri=" + HttpUtils.urlEncode(getAuthResponseUrl()) // TODO: protocol
				+ "&response_type=code"
				+ "&scope=identify email");
	}

	@Override
	public WebinterfaceAccountData handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		String code = c.getClientHeader().getPath().getGetParameterValue("code");
		
		try {
			HttpPost p = HttpRequest.createPost(TOKEN_ENDPOINT)
					.setPostParameter("client_id", clientID)
					.setPostParameter("client_secret", clientSecret)
					.setPostParameter("grant_type", "authorization_code")
					.setPostParameter("code", code)
					.setPostParameter("redirect_uri", getAuthResponseUrl())
					.setPostParameter("scope", "identify email");
			JSONObject res = p.execute().asJSONObject();
			String accToken = res.getString("access_token");
			
			HttpGet g = HttpRequest.createGet("https://discordapp.com/api/v6/users/@me")
					.setHeaderParameter("Authorization", "Bearer " + accToken);
			JSONObject usr = g.execute().asJSONObject();
			
			String
				userID = usr.getString("id"),
				userName = usr.getString("username"),
				userEmail = usr.getString("email"),
				userAvatar = usr.getString("avatar"),
				userAvatarUrl = "https://cdn.discordapp.com/avatars/" + userID + "/" + userAvatar + (userAvatar.startsWith("a_") ? ".gif?size=64" : ".png?size=64");
			
			return new WebinterfaceAccountData(getID(), userID, userName, userEmail, userAvatarUrl);
		} catch (Exception e) {
			throw new AuthException("Failed to verify Discord auth token", e);
		}
	}
	
	@Override
	public boolean isAvailable() {
		return available;
	}
	
}
