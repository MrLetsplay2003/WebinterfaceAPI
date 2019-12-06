package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class WebinterfaceAuthRequestDocument implements HttpDocument {
	
	private WebinterfaceAuthMethod method;
	
	public WebinterfaceAuthRequestDocument(WebinterfaceAuthMethod method) {
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
