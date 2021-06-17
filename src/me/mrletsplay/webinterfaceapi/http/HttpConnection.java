package me.mrletsplay.webinterfaceapi.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.net.ssl.SSLSocket;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.misc.FriendlyException;
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
			Webinterface.getLogger().debug("Error while intializing connection", e);
		}
	}
	
	public boolean isSecure() {
		return getSocket() instanceof SSLSocket;
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
				}catch(SocketException ignored) {
					// Client probably just disconnected
				}catch(Exception e) {
					close();
					Webinterface.getLogger().debug("Error in client receive loop", e);
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
			
			sh = ctx.getServerHeader();
			
			if(sh.isAllowByteRanges()) applyRanges(sh);
			if(sh.isCompressionEnabled()) applyCompression(sh);
		}catch(Exception e) {
			Webinterface.getLogger().debug("Error while creating page content", e);

			// Reset all of the context-related fields to ensure a clean environment
			sh = new HttpServerHeader(getServer().getProtocolVersion(), HttpStatusCodes.OK_200, new HttpHeaderFields());
			ctx = new HttpRequestContext(this, h, sh);
			HttpRequestContext.setCurrentContext(ctx);
			ctx.setException(e);
			
			getServer().getDocumentProvider().get500Document().createContent();
		}
		
		if(h.getFields().getFieldValue("Connection").equalsIgnoreCase("keep-alive")) {
			sh.getFields().setFieldValue("Connection", "keep-alive");
		}

		InputStream sIn = getSocket().getInputStream();
		OutputStream sOut = getSocket().getOutputStream();
		
		sOut.write(sh.getHeaderBytes());
		sOut.flush();
		
		InputStream in = sh.getContent();
		long skipped = in.skip(sh.getContentOffset());
		if(skipped < sh.getContentOffset()) Webinterface.getLogger().debug("Could not skip to content offset (skipped " + skipped + " of " + sh.getContentOffset() + " bytes)");
		
		byte[] buf = new byte[4096];
		int len;
		int tot = 0;
		while(sIn.available() == 0 && tot < sh.getContentLength() && (len = in.read(buf, 0, (int) Math.min(buf.length, sh.getContentLength() - tot))) > 0) {
			tot += len;
			sOut.write(buf, 0, len);
		}
		
		sOut.flush();
		
		return !h.getFields().getFieldValue("Connection").equalsIgnoreCase("close");
	}
	
	private void applyRanges(HttpServerHeader sh) {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		
		Pattern byteRangePattern = Pattern.compile("bytes=(?<start>\\d+)?-(?<end>\\d+)?"); // Multipart range requests are not supported
		String range = ctx.getClientHeader().getFields().getFieldValue("Range");
		if(range != null) {
			Matcher m = byteRangePattern.matcher(range);
			if(m.matches()) {
				try {
					if(m.group("start") != null) {
						long start = Long.parseLong(m.group("start"));
						if(start >= sh.getContentLength()) throw new FriendlyException("Range");
						if(m.group("end") != null) {
							long end = Long.parseLong(m.group("end"));
							if(end >= sh.getContentLength() || end > start) throw new FriendlyException("Range");
							sh.setContentOffset(start);
							sh.setContentLength(end - start + 1);
						}else {
							sh.setContentOffset(start);
							sh.setContentLength(sh.getContentLength() - start);
						}
					}else if(m.group("end") != null) {
						long endOff = Long.parseLong(m.group("end"));
						if(endOff > sh.getContentLength()) throw new FriendlyException("Range");
						sh.setContentOffset(sh.getContentLength() - endOff);
						sh.setContentLength(endOff);
					}else {
						sh.setStatusCode(HttpStatusCodes.BAD_REQUEST_400);
						sh.setContent("text/html", "<h1>400 Bad Request</h1>".getBytes(StandardCharsets.UTF_8));
						return;
					}
					sh.setStatusCode(HttpStatusCodes.PARTIAL_CONTENT_206);
					sh.getFields().setFieldValue("Content-Range", "bytes " + sh.getContentOffset() + "-" + (sh.getContentOffset() + sh.getContentLength() - 1) + "/" + sh.getTotalContentLength());
				}catch(NumberFormatException | FriendlyException e) {
					sh.setStatusCode(HttpStatusCodes.REQUESTED_RANGE_NOT_SATISFIABLE_416);
					sh.setContent("text/html", "<h1>416 Requested Range Not Satisfiable</h1>".getBytes(StandardCharsets.UTF_8));
				}
			}
		}
	}
	
	private void applyCompression(HttpServerHeader sh) throws IOException {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		
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

}
