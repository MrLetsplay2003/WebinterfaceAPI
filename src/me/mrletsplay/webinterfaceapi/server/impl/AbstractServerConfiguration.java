package me.mrletsplay.webinterfaceapi.server.impl;

import me.mrletsplay.mrcore.misc.Builder;

public class AbstractServerConfiguration {
	
	private String host;
	private int port;
	
	protected AbstractServerConfiguration(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}

	public static abstract class AbstractServerConfigurationBuilder<T extends AbstractServerConfiguration, Self extends Builder<T,Self>> implements Builder<T, Self>{
		
		protected String host;
		protected int port;
		
		protected AbstractServerConfigurationBuilder() {}
		
		public Self host(String host) {
			this.host = host;
			return getSelf();
		}
		
		public Self hostBindAll() {
			this.host = "0.0.0.0";
			return getSelf();
		}
		
		public Self port(int port) {
			this.port = port;
			return getSelf();
		}
		
	}
	
}
