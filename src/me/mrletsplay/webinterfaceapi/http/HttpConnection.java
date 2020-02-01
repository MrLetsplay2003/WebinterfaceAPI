package me.mrletsplay.webinterfaceapi.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.webinterfaceapi.http.compression.HttpCompressionMethod;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.header.HttpClientHeader;
import me.mrletsplay.webinterfaceapi.http.header.HttpHeaderFields;
import me.mrletsplay.webinterfaceapi.http.header.HttpServerHeader;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.server.ServerException;
import me.mrletsplay.webinterfaceapi.server.connection.impl.AbstractConnection;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class HttpConnection extends AbstractConnection {

	public HttpConnection(HttpServer server, Socket socket) {
		super(server, socket);
		try {
			socket.setSoTimeout(10000);
		} catch (SocketException e) {
			Webinterface.getLogger().log(Level.FINE, "Error while intializing connection", e);
		}
	}
	
	@Override
	public void startRecieving() {
		getServer().getExecutor().submit(() -> {
			while(isSocketAlive() && !getServer().getExecutor().isShutdown()) {
				try {
					if(!receive()) {
						close();
						return;
					}
				}catch(SocketTimeoutException ignored) {
				}catch(Exception e) {
					close();
					Webinterface.getLogger().log(Level.FINE, "Error in client receive loop", e);
					throw new ServerException("Error in client receive loop", e);
				}
			}
		});
	}
	
	@Override
	public HttpServer getServer() {
		return (HttpServer) super.getServer();
	}
	
	private boolean receive() throws IOException {
		HttpClientHeader h = HttpClientHeader.parse(getSocket().getInputStream());
		if(h == null) return false;
		
		HttpServerHeader sh = new HttpServerHeader(getServer().getProtocolVersion(), HttpStatusCodes.OK_200, new HttpHeaderFields());
		HttpRequestContext ctx = new HttpRequestContext(this, h, sh);
		HttpRequestContext.setCurrentContext(ctx);
		
		HttpDocument d = getServer().getDocumentProvider().getDocument(h.getPath().getDocumentPath());
		if(d == null) d = getServer().getDocumentProvider().get404Document();
		try {
			d.createContent();
		}catch(Exception e) {
			Webinterface.getLogger().log(Level.FINE, "Error while creating page content", e);
			return false;
		}
		
		sh = ctx.getServerHeader();

		if(sh.isCompressionEnabled()) {
			List<String> supCs = Arrays.stream(ctx.getClientHeader().getFields().getFieldValue("Accept-Encoding").split(","))
					.map(String::trim)
					.collect(Collectors.toList());
			HttpCompressionMethod comp = getServer().getCompressionMethods().stream()
					.filter(c -> supCs.contains(c.getName()))
					.findFirst().orElse(null);
			if(comp != null) {
				InputStream content = sh.getContent();
				sh.getFields().setFieldValue("Content-Encoding", comp.getName());
				
				byte[] uncompressedContent = IOUtils.readAllBytes(content);
				sh.setContent(comp.compress(uncompressedContent));
			}
		}
		
		OutputStream sOut = getSocket().getOutputStream();
		
		sOut.write(sh.getHeaderBytes());
		sOut.flush();
		IOUtils.transfer(sh.getContent(), sOut);
		sOut.flush();
		
		return !h.getFields().getFieldValue("Connection").equals("close");
	}

}
