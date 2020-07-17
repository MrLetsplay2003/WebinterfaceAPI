package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceAuthResponseDocument implements HttpDocument {
	
	private WebinterfaceAuthMethod method;
	
	public WebinterfaceAuthResponseDocument(WebinterfaceAuthMethod method) {
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
			WebinterfaceAccountConnection acc = method.handleAuthResponse();
			
			WebinterfaceSession sess = WebinterfaceSession.getCurrentSession();
			if(sess != null && !acc.isTemporary() && method.getShouldConnect()) {
				WebinterfaceAccount other = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(acc.getAuthMethod(), acc.getUserID());
				
				if(other == null) {
					WebinterfaceAccount account = sess.getAccount();
					if(account.getConnection(acc.getAuthMethod()) == null) account.addConnection(acc);
				}
			}else {
				WebinterfaceSession.stopSession();
				WebinterfaceSession s = WebinterfaceSession.startSession(acc);
				c.getServerHeader().getFields().setCookie(WebinterfaceSession.COOKIE_NAME, s.getSessionID(), "Path=/", "Expires=" + WebinterfaceUtils.httpTimeStamp(s.getExpiresAt()));
			}
			
			c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			c.getServerHeader().getFields().setFieldValue("Location", method.getPostAuthRedirectURL().toString());
		} catch(AuthException e) {
			c.getServerHeader().setContent("text/plain", ("Auth failed: " + e.getMessage()).getBytes(StandardCharsets.UTF_8)); // TODO: handle exc msg
		}catch(Exception e) {
			Webinterface.getLogger().log(Level.FINE, "Unexpected authentication error", e);
			c.getServerHeader().setContent("text/plain", "Auth error".getBytes(StandardCharsets.UTF_8)); // TODO: handle exc msg
		}
	}

}
