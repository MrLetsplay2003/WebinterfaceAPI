package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import java.io.File;
import java.nio.file.Files;

import me.mrletsplay.mrcore.http.HttpGet;
import me.mrletsplay.mrcore.http.HttpPost;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.header.HttpURLPath;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class GoogleAuth implements WebinterfaceAuthMethod {

	private static final String
		AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth",
		TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token",
		USERINFO_ENDPOINT = "https://www.googleapis.com/oauth2/v2/userinfo";
	
	private boolean available;
	
	private String
		clientID,
		clientSecret;
	
	public GoogleAuth() {
		File cfgFile = new File(getConfigurationDirectory(), "credentials.json");
		try {
			IOUtils.createFile(cfgFile);
			String cont = new String(Files.readAllBytes(cfgFile.toPath()));
			JSONObject obj = new JSONObject(cont).getJSONObject("web");
			clientID = obj.getString("client_id");
			clientSecret = obj.getString("client_secret");
			if(clientID != null && clientSecret != null) available = true;
		} catch (Exception e) {
			available = false;
		}
		if(!available) Webinterface.getLogger().warning("Google auth needs to be configured");
	}
	
	@Override
	public String getID() {
		return "google";
	}

	@Override
	public String getName() {
		return "Google";
	}

	@Override
	public void handleAuthRequest() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
		c.getServerHeader().getFields().setFieldValue("Location", AUTH_ENDPOINT
				+ "?client_id=" + HttpUtils.urlEncode(clientID) // TODO: auth client id
				+ "&access_type=offline"
				+ "&redirect_uri=" + HttpUtils.urlEncode(getAuthResponseUrl().toString()) // TODO: protocol
				+ "&response_type=code"
				+ "&scope=" + HttpUtils.urlEncode("profile email openid")
				+ "&prompt=" + HttpUtils.urlEncode("select_account")
				+ "&state=" + HttpUtils.urlEncode(HttpRequestContext.getCurrentContext().getClientHeader().getPath().getQueryParameterValue("from", "/")));
	}

	@Override
	public WebinterfaceAccountConnection handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		String code = c.getClientHeader().getPath().getQueryParameterValue("code");
		
		try {
			HttpPost p = HttpRequest.createPost(TOKEN_ENDPOINT)
					.setPostParameter("code", code)
					.setPostParameter("client_id", clientID)
					.setPostParameter("client_secret", clientSecret)
					.setPostParameter("redirect_uri", getAuthResponseUrl().toString())
					.setPostParameter("grant_type", "authorization_code");
			JSONObject res = p.execute().asJSONObject();
			String accToken = res.getString("access_token");
			
			HttpGet g = HttpRequest.createGet(USERINFO_ENDPOINT)
					.setHeaderParameter("Authorization", "Bearer " + accToken);
			JSONObject usr = g.execute().asJSONObject();
			
			String
				userID = usr.getString("id"),
				userName = usr.getString("name"),
				userEmail = usr.getString("email"),
				userAvatarUrl = usr.getString("picture");
			
			return new WebinterfaceAccountConnection(getID(), userID, userName, userEmail, userAvatarUrl);
		} catch (Exception e) {
			throw new AuthException("Failed to verify Google auth token", e);
		}
	}
	
	@Override
	public HttpURLPath getPostAuthRedirectURL() {
		return HttpURLPath.of(HttpRequestContext.getCurrentContext().getClientHeader().getPath().getQueryParameterValue("state", "/"));
	}
	
	@Override
	public boolean isAvailable() {
		return available;
	}

}
