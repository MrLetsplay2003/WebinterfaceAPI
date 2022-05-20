package me.mrletsplay.webinterfaceapi.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.header.DefaultClientContentTypes;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.auth.impl.DiscordAuth;
import me.mrletsplay.webinterfaceapi.auth.impl.GitHubAuth;
import me.mrletsplay.webinterfaceapi.auth.impl.GoogleAuth;
import me.mrletsplay.webinterfaceapi.auth.impl.PasswordAuth;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class SetupSubmitDocument implements HttpDocument {

	@Override
	public void createContent() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		URLEncoded params = ctx.getClientHeader().getPostData().getParsedAs(DefaultClientContentTypes.URLENCODED);

		Integer currentStep = Webinterface.getConfig().getOverride(SetupDocument.SETUP_STEP_OVERRIDE_PATH, Integer.class);
		if(currentStep == null) currentStep = SetupDocument.SETUP_STEP_BASE;

		switch(currentStep) {
			case SetupDocument.SETUP_STEP_BASE:
			{
				String username = params.getFirst("admin-name").trim();
				if(!PasswordAuth.isValidUsername(username)) {
					error("Invalid username");
					return;
				}

				String password = params.getFirst("admin-password").trim();
				Account acc = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(PasswordAuth.ID, username, true);
				if(acc != null) {
					error("Account already exists");
					return;
				}

				Webinterface.getCredentialsStorage().storeCredentials(username, password);
				AccountConnection con = new AccountConnection(PasswordAuth.ID, username, username, null, null);
				acc = Webinterface.getAccountStorage().createAccount();
				acc.addConnection(con);
				acc.addPermission("*");
				Webinterface.getConfig().setOverride(SetupDocument.SETUP_STEP_OVERRIDE_PATH, SetupDocument.SETUP_STEP_HTTP);
				break;
			}
			case SetupDocument.SETUP_STEP_HTTP:
			{
				String httpBind = params.getFirst("http-bind");
				String httpHost = params.getFirst("http-host");
				int httpPort = Integer.parseInt(params.getFirst("http-port"));

				Webinterface.getConfig().setSetting(DefaultSettings.HTTP_BIND, httpBind);
				Webinterface.getConfig().setSetting(DefaultSettings.HTTP_HOST, httpHost);
				Webinterface.getConfig().setSetting(DefaultSettings.HTTP_PORT, httpPort);

				boolean enableHttps = params.has("enable-https") && params.getFirst("enable-https").equals("on");
				if(enableHttps) {
					String httpsBind = params.getFirst("https-bind");
					String httpsHost = params.getFirst("https-host");
					int httpsPort = Integer.parseInt(params.getFirst("https-port"));

					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_BIND, httpsBind);
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_HOST, httpsHost);
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_PORT, httpsPort);

					String certPath = params.getFirst("https-cert-path");
					String certPass = params.getFirst("https-cert-password");
					String certKeyPath = params.getFirst("https-cert-key-path");

					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_CERTIFICATE_PATH, certPath);
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_CERTIFICATE_PASSWORD, certPass);
					Webinterface.getConfig().setSetting(DefaultSettings.HTTPS_CERTIFICATE_KEY_PATH, certKeyPath);
				}

				Webinterface.getConfig().setOverride(SetupDocument.SETUP_STEP_OVERRIDE_PATH, SetupDocument.SETUP_STEP_AUTH);
				break;
			}
			case SetupDocument.SETUP_STEP_AUTH:
			{
				boolean noAuth = params.has("no-auth") && params.getFirst("no-auth").equals("on");
				Webinterface.getConfig().setSetting(DefaultSettings.ALLOW_ANONYMOUS, noAuth);

				boolean discordAuth = params.has("discord-auth") && params.getFirst("discord-auth").equals("on");
				Webinterface.getConfig().setSetting(DefaultSettings.ENABLE_DISCORD_AUTH, discordAuth);
				if(discordAuth) {
					String clientID = params.getFirst("discord-client-id").trim();
					String clientSecret = params.getFirst("discord-client-secret").trim();
					DiscordAuth a = (DiscordAuth) Webinterface.getAuthMethodByID(DiscordAuth.ID);
					a.setup(clientID, clientSecret);
				}

				boolean googleAuth = params.has("google-auth") && params.getFirst("google-auth").equals("on");
				Webinterface.getConfig().setSetting(DefaultSettings.ENABLE_GOOGLE_AUTH, googleAuth);
				if(googleAuth) {
					String clientID = params.getFirst("google-client-id").trim();
					String clientSecret = params.getFirst("google-client-secret").trim();
					GoogleAuth a = (GoogleAuth) Webinterface.getAuthMethodByID(GoogleAuth.ID);
					a.setup(clientID, clientSecret);
				}

				boolean githubAuth = params.has("github-auth") && params.getFirst("github-auth").equals("on");
				Webinterface.getConfig().setSetting(DefaultSettings.ENABLE_GITHUB_AUTH, githubAuth);
				if(githubAuth) {
					String clientID = params.getFirst("github-client-id").trim();
					String clientSecret = params.getFirst("github-client-secret").trim();
					GitHubAuth a = (GitHubAuth) Webinterface.getAuthMethodByID(GitHubAuth.ID);
					a.setup(clientID, clientSecret);
				}

				Webinterface.getConfig().setOverride(SetupDocument.SETUP_STEP_OVERRIDE_PATH, SetupDocument.SETUP_STEP_DONE);
				ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
				ctx.getServerHeader().getFields().set("Location", "/");
				Webinterface.getDocumentProvider().unregisterDocument("/setup");
				Webinterface.getDocumentProvider().unregisterDocument("/setup/submit");
				return;
			}
		}

		ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
		ctx.getServerHeader().getFields().set("Location", "/setup");
	}

	private void error(String message) {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		ctx.getServerHeader().setStatusCode(HttpStatusCodes.BAD_REQUEST_400);
		ctx.getServerHeader().setContent("text/plain", message.getBytes(StandardCharsets.UTF_8));
	}

}
