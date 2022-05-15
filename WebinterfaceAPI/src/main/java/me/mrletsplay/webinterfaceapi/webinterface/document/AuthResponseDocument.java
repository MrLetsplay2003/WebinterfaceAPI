package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.Account;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.session.Session;

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
			AccountConnection acc = method.handleAuthResponse();

			Session sess = Session.getCurrentSession();
			if(sess != null && !acc.isTemporary() && method.getShouldConnect()) {
				Account other = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(acc.getConnectionName(), acc.getUserID());

				if(other == null) {
					Account account = sess.getAccount();
					if(account.getConnection(acc.getConnectionName()) == null) account.addConnection(acc);
				}
			}else {
				Session.stopSession();
				Session s = Session.startSession(acc);
				c.getServerHeader().getFields().setCookie(Session.COOKIE_NAME, s.getSessionID(), "Path=/", "Expires=" + WebinterfaceUtils.httpTimeStamp(s.getExpiresAt()));
			}

			c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			c.getServerHeader().getFields().set("Location", method.getPostAuthRedirectURL().toString());
		} catch(AuthException e) {
			c.getServerHeader().setContent("text/plain", ("Auth failed: " + e.getMessage()).getBytes(StandardCharsets.UTF_8)); // TODO: handle exc msg
		}catch(Exception e) {
			Webinterface.getLogger().debug("Unexpected authentication error", e);
			c.getServerHeader().setContent("text/plain", "Auth error".getBytes(StandardCharsets.UTF_8)); // TODO: handle exc msg
		}
	}

}
