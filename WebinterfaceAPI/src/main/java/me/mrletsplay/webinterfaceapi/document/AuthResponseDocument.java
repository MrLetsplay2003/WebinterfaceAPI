package me.mrletsplay.webinterfaceapi.document;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.auth.AuthException;
import me.mrletsplay.webinterfaceapi.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.session.Session;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class AuthResponseDocument implements HttpDocument {

	private AuthMethod method;

	public AuthResponseDocument(AuthMethod method) {
		this.method = method;
	}

	@Override
	public void createContent() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		if(!method.isAvailable()) {
			c.getServerHeader().setContent("text/plain", "Auth method unavailable".getBytes(StandardCharsets.UTF_8));
			return;
		}

		try {
			AccountConnection connection = method.handleAuthResponse();

			Session sess = Session.getCurrentSession();
			if(sess != null && method.canConnect() && method.getShouldConnect()) {
				Account other = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(connection.getConnectionName(), connection.getUserID());

				if(other == null) {
					Account account = sess.getAccount();
					if(account.getConnection(connection.getConnectionName()) == null) account.addConnection(connection);
				}
			}else {
				Session.stopSession();

				Account account = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(connection.getConnectionName(), connection.getUserID());
				if(account == null) {
					String regMode = Webinterface.getConfig().getSetting(DefaultSettings.REGISTRATION_MODE);
					if(!"enable".equals(regMode)) {
						if("secret".equals(regMode)) {
							// Redirect
							String id = UUID.randomUUID().toString();
							RegistrationSecretDocument.ACTIVE_REGISTRATIONS.put(id, connection);

							c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
							c.getServerHeader().getFields().set("Location", "/registration-secret?id=" + id);
						}

						throw new AuthException("Registration is disabled");
					}
				}

				Session s = Session.startSession(connection);
				c.getServerHeader().getFields().setCookie(Session.COOKIE_NAME, s.getSessionID(), "Path=/", "Expires=" + WebinterfaceUtils.httpTimeStamp(s.getExpiresAt()));
			}

			c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			c.getServerHeader().getFields().set("Location", method.getPostAuthRedirectURL().toString());
		} catch(AuthException e) {
			c.getServerHeader().setContent("text/plain", ("Auth failed: " + e.getMessage()).getBytes(StandardCharsets.UTF_8)); // TODO: handle exc msg
		}catch(Exception e) {
			Webinterface.getLogger().error("Unexpected authentication error", e);
			c.getServerHeader().setContent("text/plain", "Auth error".getBytes(StandardCharsets.UTF_8)); // TODO: handle exc msg
		}
	}

}
