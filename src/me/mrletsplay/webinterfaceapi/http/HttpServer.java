package me.mrletsplay.webinterfaceapi.http;

import me.mrletsplay.webinterfaceapi.http.document.DefaultDocumentProvider;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocumentProvider;
import me.mrletsplay.webinterfaceapi.server.impl.AbstractServer;

public class HttpServer extends AbstractServer {
	
	private HttpDocumentProvider documentProvider;
	private HttpProtocolVersion protocolVersion;

	public HttpServer(int port) {
		super(port);
		this.protocolVersion = HttpProtocolVersions.HTTP1_1;
		setConnectionAcceptor(new HttpConnectionAcceptor());
		setDocumentProvider(new DefaultDocumentProvider());
	}
	
	public void setDocumentProvider(HttpDocumentProvider documentProvider) throws IllegalStateException {
		if(isRunning()) throw new IllegalStateException("Server is running");
		this.documentProvider = documentProvider;
	}
	
	public HttpDocumentProvider getDocumentProvider() {
		return documentProvider;
	}
	
	public HttpProtocolVersion getProtocolVersion() {
		return protocolVersion;
	}

}
