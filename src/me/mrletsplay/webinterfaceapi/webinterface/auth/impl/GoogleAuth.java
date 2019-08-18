package me.mrletsplay.webinterfaceapi.webinterface.auth.impl;

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

public class GoogleAuth implements WebinterfaceAuthMethod {

	private static final String
		AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth",
		TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token",
		USERINFO_ENDPOINT = "https://www.googleapis.com/oauth2/v2/userinfo",
		CLIENT_ID = "251901702956-57o5tp46mlp3intf4b88kk4u0ag0o0tk.apps.googleusercontent.com",
		CLIENT_SECRET = "czYC-HTsGYd9ZfsLR3GwiAOb";
	
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
				+ "?client_id=" + HttpUtils.urlEncode(CLIENT_ID) // TODO: auth client id
				+ "&access_type=offline"
				+ "&redirect_uri=" + HttpUtils.urlEncode(getAuthResponseUrl()) // TODO: protocol
				+ "&response_type=code"
				+ "&scope=" + HttpUtils.urlEncode("profile email openid")
				+ "&prompt=" + HttpUtils.urlEncode("select_account"));
	}

	@Override
	public WebinterfaceAccountData handleAuthResponse() throws AuthException {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		String code = c.getClientHeader().getPath().getGetParameterValue("code");
		try {
			HttpPost p = HttpRequest.createPost(TOKEN_ENDPOINT)
					.setPostParameter("code", code)
					.setPostParameter("client_id", CLIENT_ID)
					.setPostParameter("client_secret", CLIENT_SECRET)
					.setPostParameter("redirect_uri", getAuthResponseUrl())
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
			
			return new WebinterfaceAccountData(userID, userName, userEmail, userAvatarUrl);
		} catch (Exception e) {
			throw new AuthException("Failed to verify Google auth token", e);
		}
	}

}
