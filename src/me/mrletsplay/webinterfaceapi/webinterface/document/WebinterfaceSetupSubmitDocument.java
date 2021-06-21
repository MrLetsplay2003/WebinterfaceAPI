package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.header.HttpClientContentTypes;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.DiscordAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.GitHubAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.GoogleAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.PasswordAuth;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class WebinterfaceSetupSubmitDocument implements HttpDocument {
	
	@SuppressWarnings("unchecked")
	@Override
	public void createContent() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		Map<String, List<String>> params = (Map<String, List<String>>) ctx.getClientHeader().getPostData().getParsedAs(HttpClientContentTypes.URLENCODED);
		
		Integer currentStep = Webinterface.getConfig().getOverride(WebinterfaceSetupDocument.SETUP_STEP_OVERRIDE_PATH, Integer.class);
		if(currentStep == null) currentStep = WebinterfaceSetupDocument.SETUP_STEP_BASE;
		
		switch(currentStep) {
			case WebinterfaceSetupDocument.SETUP_STEP_BASE:
			{
				String username = params.get("admin-name").get(0).trim();
				if(!PasswordAuth.isValidUsername(username)) {
					error("Invalid username");
					return;
				}
				
				String password = params.get("admin-password").get(0).trim();
				WebinterfaceAccount acc = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(PasswordAuth.ID, username, true);
				if(acc != null) {
					error("Account already exists");
					return;
				}
				
				Webinterface.getCredentialsStorage().storeCredentials(username, password);
				WebinterfaceAccountConnection con = new WebinterfaceAccountConnection(PasswordAuth.ID, username, username, null, null);
				acc = Webinterface.getAccountStorage().createAccount();
				acc.addConnection(con);
				acc.addPermission("*");
				Webinterface.getConfig().setOverride(WebinterfaceSetupDocument.SETUP_STEP_OVERRIDE_PATH, WebinterfaceSetupDocument.SETUP_STEP_HTTP);
				break;
			}
			case WebinterfaceSetupDocument.SETUP_STEP_HTTP:
			{
				String httpBind = params.get("http-bind").get(0);
				String httpHost = params.get("http-host").get(0);
				int httpPort = Integer.parseInt(params.get("http-port").get(0));
				
				Webinterface.getConfig().setSetting(DefaultSettings.HTTP_BIND, httpBind);
				Webinterface.getConfig().setSetting(DefaultSettings.HTTP_HOST, httpHost);
				Webinterface.getConfig().setSetting(DefaultSettings.HTTP_PORT, httpPort);
				
				boolean enableHttps = params.containsKey("enable-https") && params.get("enable-https").get(0).equals("on");
				if(enableHttps) {
					String httpsBind = params.get("https-bind").get(0);
					String httpsHost = params.get("https-host").get(0);
					int httpsPort = Integer.parseInt(params.get("https-port").get(0));
					
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_BIND, httpsBind);
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_HOST, httpsHost);
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_PORT, httpsPort);
					
					String certPath = params.get("https-cert-path").get(0);
					String certPass = params.get("https-cert-password").get(0);
					String certKeyPath = params.get("https-cert-key-path").get(0);
					
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_CERTIFICATE_PATH, certPath);
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_CERTIFICATE_PASSWORD, certPass);
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_CERTIFICATE_KEY_PATH, certKeyPath);
				}
				
				Webinterface.getConfig().setOverride(WebinterfaceSetupDocument.SETUP_STEP_OVERRIDE_PATH, WebinterfaceSetupDocument.SETUP_STEP_AUTH);
				break;
			}
			case WebinterfaceSetupDocument.SETUP_STEP_AUTH:
			{
				boolean noAuth = !params.get("no-auth").isEmpty() && params.get("no-auth").get(0).equals("on");
				Webinterface.getConfig().setSetting(DefaultSettings.ALLOW_ANONYMOUS, noAuth);
				
				boolean discordAuth = params.containsKey("discord-auth") && params.get("discord-auth").get(0).equals("on");
				Webinterface.getConfig().setSetting(DefaultSettings.ENABLE_DISCORD_AUTH, discordAuth);
				if(discordAuth) {
					String clientID = params.get("discord-client-id").get(0).trim();
					String clientSecret = params.get("discord-client-secret").get(0).trim();
					DiscordAuth a = (DiscordAuth) Webinterface.getAuthMethodByID(DiscordAuth.ID);
					a.setup(clientID, clientSecret);
				}
				
				boolean googleAuth = params.containsKey("google-auth") && params.get("google-auth").get(0).equals("on");
				Webinterface.getConfig().setSetting(DefaultSettings.ENABLE_GOOGLE_AUTH, googleAuth);
				if(googleAuth) {
					String clientID = params.get("google-client-id").get(0).trim();
					String clientSecret = params.get("google-client-secret").get(0).trim();
					GoogleAuth a = (GoogleAuth) Webinterface.getAuthMethodByID(GoogleAuth.ID);
					a.setup(clientID, clientSecret);
				}
				
				boolean githubAuth = params.containsKey("github-auth") && params.get("github-auth").get(0).equals("on");
				Webinterface.getConfig().setSetting(DefaultSettings.ENABLE_GITHUB_AUTH, githubAuth);
				if(githubAuth) {
					String clientID = params.get("github-client-id").get(0).trim();
					String clientSecret = params.get("github-client-secret").get(0).trim();
					GitHubAuth a = (GitHubAuth) Webinterface.getAuthMethodByID(GitHubAuth.ID);
					a.setup(clientID, clientSecret);
				}
				
				Webinterface.getConfig().setOverride(WebinterfaceSetupDocument.SETUP_STEP_OVERRIDE_PATH, WebinterfaceSetupDocument.SETUP_STEP_DONE);
				ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
				ctx.getServerHeader().getFields().setFieldValue("Location", "/");
				Webinterface.getDocumentProvider().unregisterDocument("/setup");
				Webinterface.getDocumentProvider().unregisterDocument("/setup/submit");
				return;
			}
		}
		
		ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
		ctx.getServerHeader().getFields().setFieldValue("Location", "/setup");
	}
	
	private void error(String message) {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		ctx.getServerHeader().setStatusCode(HttpStatusCodes.BAD_REQUEST_400);
		ctx.getServerHeader().setContent("text/plain", message.getBytes(StandardCharsets.UTF_8));
	}
	
}
