package me.mrletsplay.webinterfaceapi.http;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.mrletsplay.webinterfaceapi.http.compression.DeflateCompression;
import me.mrletsplay.webinterfaceapi.http.compression.GZIPCompression;
import me.mrletsplay.webinterfaceapi.http.compression.HttpCompressionMethod;
import me.mrletsplay.webinterfaceapi.http.document.DefaultDocumentProvider;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocumentProvider;
import me.mrletsplay.webinterfaceapi.server.impl.AbstractServer;

public class HttpServer extends AbstractServer {
	
	private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getPackage().getName());
	
	private HttpDocumentProvider documentProvider;
	private HttpProtocolVersion protocolVersion;
	private List<HttpCompressionMethod> compressionMethods;

	public HttpServer(String host, int port) {
		super(host, port);
		this.protocolVersion = HttpProtocolVersions.HTTP1_1;
		this.compressionMethods = new ArrayList<>();
		setConnectionAcceptor(new HttpConnectionAcceptor());
		setDocumentProvider(new DefaultDocumentProvider());
		addCompressionMethod(new DeflateCompression());
		addCompressionMethod(new GZIPCompression());
	}
	
	public HttpServer(int port) {
		this("0.0.0.0", port);
	}
	
	public void setDocumentProvider(HttpDocumentProvider documentProvider) throws IllegalStateException {
		if(isRunning()) throw new IllegalStateException("Server is running");
		this.documentProvider = documentProvider;
	}
	
	public HttpDocumentProvider getDocumentProvider() {
		return documentProvider;
	}
	
	public void addCompressionMethod(HttpCompressionMethod compressionMethod) {
		compressionMethods.add(compressionMethod);
	}
	
	public List<HttpCompressionMethod> getCompressionMethods() {
		return compressionMethods;
	}
	
	public HttpProtocolVersion getProtocolVersion() {
		return protocolVersion;
	}
	
	public static Logger getLogger() {
		return LOGGER;
	}

}
