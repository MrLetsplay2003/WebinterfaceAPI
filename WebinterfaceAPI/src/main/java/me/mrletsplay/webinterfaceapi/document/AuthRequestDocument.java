package me.mrletsplay.webinterfaceapi.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.util.MimeType;
import me.mrletsplay.webinterfaceapi.auth.AuthMethod;

public class AuthRequestDocument implements HttpDocument {

	private AuthMethod method;

	public AuthRequestDocument(AuthMethod method) {
		this.method = method;
	}

	@Override
	public void createContent() {
		if(!method.isAvailable()) {
			HttpRequestContext c = HttpRequestContext.getCurrentContext();
			c.getServerHeader().setContent(MimeType.TEXT, "Auth method unavailable".getBytes(StandardCharsets.UTF_8));
			return;
		}
		method.handleAuthRequest();
	}

}
