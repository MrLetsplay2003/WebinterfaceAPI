package me.mrletsplay.webinterfaceapi.http;

import me.mrletsplay.webinterfaceapi.server.impl.AbstractServerConfiguration;

public class HttpServerConfiguration extends AbstractServerConfiguration {

	protected HttpServerConfiguration(String host, int port) {
		super(host, port);
	}
	
	public static class Builder extends AbstractServerConfigurationBuilder<HttpServerConfiguration, Builder> {

		@Override
		public HttpServerConfiguration create() throws IllegalStateException {
			return new HttpServerConfiguration(host, port);
		}
		
	}

}
