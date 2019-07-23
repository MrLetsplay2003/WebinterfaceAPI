package me.mrletsplay.webinterfaceapi.http.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.header.HttpServerHeader;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;

public class Default404Document implements HttpDocument {

	@Override
	public void createContent() {
		HttpServerHeader h = HttpRequestContext.getCurrentContext().getServerHeader();
		h.setStatusCode(HttpStatusCodes.NOT_FOUND_404);
		h.setContent("text/html",
				("<!DOCTYPE html><html><body><h1>404 Not Found</h1><br/>"
				+ "<i>" + HttpRequestContext.getCurrentContext().getClientHeader().getPath().getDocumentPath() + " was not found on this server</i></body></html>")
				.getBytes(StandardCharsets.UTF_8));
	}

}
