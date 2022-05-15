package me.mrletsplay.webinterfaceapi.webinterface.document;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.session.Session;

public class WebinterfaceLogoutDocument implements HttpDocument {

	@Override
	public void createContent() {
		Session.stopSession();
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
		c.getServerHeader().getFields().set("Location", "/");
	}

}
