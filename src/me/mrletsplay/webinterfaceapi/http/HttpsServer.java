package me.mrletsplay.webinterfaceapi.http;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;

import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.webinterfaceapi.http.ssl.SSLCertificateSocketFactory;

public class HttpsServer extends HttpServer {
	
	private SSLCertificateSocketFactory socketFactory;

	public HttpsServer(HttpsServerConfiguration configuration) {
		super(configuration);
		try {
			this.socketFactory = new SSLCertificateSocketFactory(configuration.getCertificateFile(), configuration.getCertificateKeyFile(), configuration.getCertificatePassword());
		} catch (IOException | GeneralSecurityException e) {
			throw new FriendlyException("Failed to intialize http server", e);
		}
	}

	@Deprecated
	public HttpsServer(String host, int port, File certificateFile, File keyFile, String certificatePassword) {
		this(newConfigurationBuilder()
				.host(host)
				.port(port)
				.certificate(certificateFile, keyFile)
				.certificatePassword(certificatePassword)
				.create());
	}

	@Deprecated
	public HttpsServer(int port, File certificateFile, File keyFile, String certificatePassword) {
		this(newConfigurationBuilder()
				.hostBindAll()
				.port(port)
				.certificate(certificateFile, keyFile)
				.certificatePassword(certificatePassword)
				.create());
	}
	
	@Override
	protected ServerSocket createSocket() throws UnknownHostException, IOException {
		return socketFactory.createServerSocket(getConfiguration().getHost(), getConfiguration().getPort());
	}
	
	public static HttpsServerConfiguration.Builder newConfigurationBuilder() {
		return new HttpsServerConfiguration.Builder();
	}

}
