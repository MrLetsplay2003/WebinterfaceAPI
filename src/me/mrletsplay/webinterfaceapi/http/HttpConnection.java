package me.mrletsplay.webinterfaceapi.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.header.HttpClientHeader;
import me.mrletsplay.webinterfaceapi.http.header.HttpHeaderFields;
import me.mrletsplay.webinterfaceapi.http.header.HttpServerHeader;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.server.ServerException;
import me.mrletsplay.webinterfaceapi.server.connection.impl.AbstractConnection;

public class HttpConnection extends AbstractConnection {

	public HttpConnection(HttpServer server, Socket socket) {
		super(server, socket);
	}
	
	@Override
	public void startRecieving() {
		getServer().getExecutor().submit(() -> {
			while(!getServer().getExecutor().isShutdown()) {
				try {
					receive();
				}catch(Exception e) {
					close();
					e.printStackTrace(); // TODO: remove
					throw new ServerException("Error in client receive loop", e);
				}
			}
		});
	}
	
	@Override
	public HttpServer getServer() {
		return (HttpServer) super.getServer();
	}
	
	private void receive() throws IOException {
		byte[] rec = IOUtils.readBytesUntilUnavailable(getSocket().getInputStream());
		HttpClientHeader h = HttpClientHeader.parse(rec);
		if(h == null) return;
		
		HttpServerHeader sh = new HttpServerHeader(getServer().getProtocolVersion(), HttpStatusCodes.OK_200, new HttpHeaderFields());
		HttpRequestContext ctx = new HttpRequestContext(this, h, sh);
		HttpRequestContext.setCurrentContext(ctx);
		
		HttpDocument d = getServer().getDocumentProvider().getDocument(h.getPath().getDocumentPath());
		if(d == null) d = getServer().getDocumentProvider().get404Document();
		d.createContent();
		sh = ctx.getServerHeader();
		
		IOUtils.transfer(new ByteArrayInputStream(sh.toByteArray()), getSocket().getOutputStream());
		getSocket().getOutputStream().flush();
	}

}
