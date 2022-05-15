package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class AuthRequestDocument implements HttpDocument {

	private WebinterfaceAuthMethod method;

	public AuthRequestDocument(WebinterfaceAuthMethod method) {
		this.method = method;
	}

	@Override
	public void createContent() {
		if(!method.isAvailable()) {
			HttpRequestContext c = HttpRequestContext.getCurrentContext();
			c.getServerHeader().setContent("text/plain", "Auth method unavailable".getBytes(StandardCharsets.UTF_8));
			return;
		}
		method.handleAuthRequest();
	}

}
