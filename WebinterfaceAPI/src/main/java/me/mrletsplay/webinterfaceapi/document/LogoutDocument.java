package me.mrletsplay.webinterfaceapi.document;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.session.Session;

public class LogoutDocument implements HttpDocument {

	@Override
	public void createContent() {
		Session.stopSession();
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
		c.getServerHeader().getFields().set("Location", "/");
	}

}
