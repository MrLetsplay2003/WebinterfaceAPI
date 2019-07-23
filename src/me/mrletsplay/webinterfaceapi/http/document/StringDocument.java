package me.mrletsplay.webinterfaceapi.http.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;

public class StringDocument implements HttpDocument {

	private String content;
	
	public StringDocument(String content) {
		this.content = content;
	}
	
	@Override
	public void createContent() {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent(content.getBytes(StandardCharsets.UTF_8));
	}

}
