package me.mrletsplay.webinterfaceapi.http.header;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import me.mrletsplay.webinterfaceapi.http.HttpProtocolVersion;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCode;

public class HttpServerHeader {

	private HttpProtocolVersion protocolVersion;
	private HttpStatusCode statusCode;
	private HttpHeaderFields fields;
	private InputStream content;
	private long contentLength;
	private boolean compressionEnabled;
	
	public HttpServerHeader(HttpProtocolVersion protocolVersion, HttpStatusCode statusCode, HttpHeaderFields fields) {
		this.protocolVersion = protocolVersion;
		this.statusCode = statusCode;
		this.fields = fields;
		this.compressionEnabled = true;
		setContent(new byte[0]);
	}
	
	public void setProtocolVersion(HttpProtocolVersion protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	public HttpProtocolVersion getProtocolVersion() {
		return protocolVersion;
	}
	
	public void setStatusCode(HttpStatusCode statusCode) {
		this.statusCode = statusCode;
	}
	
	public HttpStatusCode getStatusCode() {
		return statusCode;
	}
	
	public HttpHeaderFields getFields() {
		return fields;
	}
	
	public void setContent(byte[] content) {
		setContent(null, content, false);
	}
	
	public void setContent(String type, byte[] content) {
		setContent(type, content, true);
	}
	
	public void setContent(String type, byte[] content, boolean forceContentType) {
		setContent(type, new ByteArrayInputStream(content), content.length, forceContentType);
	}
	
	public void setContent(InputStream content, long length) {
		setContent(null, content, length, false);
	}
	
	public void setContent(String type, InputStream content, long length) {
		setContent(type, content, length, true);
	}
	
	public void setContent(String type, InputStream content, long length, boolean forceContentType) {
		if(fields.getFieldValues("Content-Type").isEmpty() || forceContentType) {
			fields.setFieldValue("Content-Type", type == null ? "application/unknown" : (type + "; charset=utf-8")); // TODO: charset?
		}
		fields.setFieldValue("Content-Length", String.valueOf(length));
		this.content = content;
		this.contentLength = length;
	}
	
	public InputStream getContent() {
		return content;
	}
	
	public long getContentLength() {
		return contentLength;
	}
	
	public void setCompressionEnabled(boolean compressionEnabled) {
		this.compressionEnabled = compressionEnabled;
	}
	
	public boolean isCompressionEnabled() {
		return compressionEnabled;
	}
	
	public byte[] getHeaderBytes() {
		String header = protocolVersion.getVersionString() + " " + statusCode.getStatusCode() + " " + statusCode.getStatusMessage() + "\r\n";
		for(Map.Entry<String, List<String>> f : fields.getFields().entrySet()) {
			for(String v : f.getValue()) {
				header += f.getKey() + ": " + v + "\r\n";
			}
		}
		header += "\r\n";
		return header.getBytes(StandardCharsets.UTF_8);
	}
	
}
