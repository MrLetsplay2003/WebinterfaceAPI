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

	public HttpsServer(String host, int port, File certificateFile, File keyFile, String certificatePassword) {
		super(host, port);
		try {
			this.socketFactory = new SSLCertificateSocketFactory(certificateFile, keyFile, certificatePassword);
		} catch (IOException | GeneralSecurityException e) {
			throw new FriendlyException("Failed to intialize http server", e);
		}
	}

	public HttpsServer(int port, File certificateFile, File keyFile, String certificatePassword) {
		this("0.0.0.0", port, certificateFile, keyFile, certificatePassword);
	}
	
	@Override
	protected ServerSocket createSocket() throws UnknownHostException, IOException {
		return socketFactory.createServerSocket(getHost(), getPort());
	}

}
