package me.mrletsplay.webinterfaceapi.http;

import java.io.File;

public class HttpsServerConfiguration extends HttpServerConfiguration {

	private File
		certificateFile,
		certificateKeyFile;
	
	private String certificatePassword;
	
	protected HttpsServerConfiguration(String host, int port, File certificateFile, File certificateKeyFile, String certificatePassword) {
		super(host, port);
		this.certificateFile = certificateFile;
		this.certificateKeyFile = certificateKeyFile;
		this.certificatePassword = certificatePassword;
	}
	
	public File getCertificateFile() {
		return certificateFile;
	}
	
	public File getCertificateKeyFile() {
		return certificateKeyFile;
	}
	
	public String getCertificatePassword() {
		return certificatePassword;
	}
	
	public static class Builder extends HttpServerConfiguration.Builder {

		private File
			certificateFile,
			certificateKeyFile;
		
		private String certificatePassword;
		
		@Override
		public Builder host(String host) {
			return (Builder) super.host(host);
		}
		
		@Override
		public Builder hostBindAll() {
			return (Builder) super.hostBindAll();
		}
		
		@Override
		public Builder port(int port) {
			return (Builder) super.port(port);
		}
		
		public Builder certificate(File certificateFile, File certificateKeyFile) {
			this.certificateFile = certificateFile;
			this.certificateKeyFile = certificateKeyFile;
			return this;
		}
		
		public Builder certificatePassword(String certificatePassword) {
			this.certificatePassword = certificatePassword;
			return this;
		}

		@Override
		public HttpsServerConfiguration create() throws IllegalStateException {
			return new HttpsServerConfiguration(host, port, certificateFile, certificateKeyFile, certificatePassword);
		}
		
	}

}
