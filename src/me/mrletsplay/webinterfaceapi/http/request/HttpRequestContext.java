package me.mrletsplay.webinterfaceapi.http.request;

import me.mrletsplay.webinterfaceapi.http.HttpConnection;
import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.header.HttpClientHeader;
import me.mrletsplay.webinterfaceapi.http.header.HttpServerHeader;

public class HttpRequestContext {
	
	private static ThreadLocal<HttpRequestContext> context = new ThreadLocal<>();
	
	private HttpConnection connection;
	private HttpClientHeader clientHeader;
	private HttpServerHeader serverHeader;
	
	public HttpRequestContext(HttpConnection connection, HttpClientHeader clientHeader, HttpServerHeader serverHeader) {
		this.connection = connection;
		this.clientHeader = clientHeader;
		this.serverHeader = serverHeader;
	}
	
	public HttpConnection getConnection() {
		return connection;
	}
	
	public HttpServer getServer() {
		return connection.getServer();
	}
	
	public HttpClientHeader getClientHeader() {
		return clientHeader;
	}
	
	public void setServerHeader(HttpServerHeader serverHeader) {
		this.serverHeader = serverHeader;
	}
	
	public HttpServerHeader getServerHeader() {
		return serverHeader;
	}
	
	public static void setCurrentContext(HttpRequestContext ctx) {
		context.set(ctx);
	}
	
	public static HttpRequestContext getCurrentContext() {
		HttpRequestContext ctx = context.get();
		if(ctx == null) throw new IllegalStateException("No context present");
		return ctx;
	}

}
