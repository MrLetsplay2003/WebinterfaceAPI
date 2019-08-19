package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

import me.mrletsplay.mrcore.http.HttpGet;
import me.mrletsplay.mrcore.http.HttpPost;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountData;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class GitHubAuth implements WebinterfaceAuthMethod {

	private static final String
		AUTH_ENDPOINT = "https://github.com/login/oauth/authorize",
		TOKEN_ENDPOINT = "https://github.com/login/oauth/access_token",
		USERINFO_ENDPOINT = "https://api.github.com/user",
		USER_EMAILS_ENDPOINT = "https://api.github.com/user/emails",
		CLIENT_ID = "3b4c78057d9ef87feb8a",
		CLIENT_SECRET = "474606dade26337b6b7c5cb43c10f879b8fbed8e";
	
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
		c.getServerHeader().getFields().setFieldValue("Location", AUTH_ENDPOINT
				+ "?client_id=" + HttpUtils.urlEncode(CLIENT_ID) // TODO: auth client id
				+ "&redirect_uri=" + HttpUtils.urlEncode(getAuthResponseUrl()) // TODO: protocol
				+ "&response_type=code"
				+ "&scope=" + HttpUtils.urlEncode("read:user user:email"));
	}

	@Override
	public WebinterfaceAccountData handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		String code = c.getClientHeader().getPath().getGetParameterValue("code");
		try {
			HttpPost p = HttpRequest.createPost(TOKEN_ENDPOINT)
					.setHeaderParameter("Accept", "application/json")
					.setPostParameter("code", code)
					.setPostParameter("client_id", CLIENT_ID)
					.setPostParameter("client_secret", CLIENT_SECRET)
					.setPostParameter("redirect_uri", getAuthResponseUrl())
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
			
			return new WebinterfaceAccountData(getID(), userID, userName, userEmail, userAvatarUrl);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuthException("Failed to verify GitHub auth token", e);
		}
	}

}
