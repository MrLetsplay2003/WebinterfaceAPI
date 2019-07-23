package me.mrletsplay.webinterfaceapi.http.header;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import me.mrletsplay.webinterfaceapi.http.HttpProtocolVersion;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCode;

public class HttpServerHeader {

	private HttpProtocolVersion protocolVersion;
	private HttpStatusCode statusCode;
	private HttpHeaderFields fields;
	private byte[] content;
	
	public HttpServerHeader(HttpProtocolVersion protocolVersion, HttpStatusCode statusCode, HttpHeaderFields fields) {
		this.protocolVersion = protocolVersion;
		this.statusCode = statusCode;
		this.fields = fields;
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
		setContent(null, content);
	}
	
	public void setContent(String type, byte[] content) {
		fields.addFieldValue("Content-Type", type == null ? "application/unknown" : (type + "; charset=utf-8"));
		fields.addFieldValue("Content-Length", String.valueOf(content.length));
		this.content = content;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public byte[] toByteArray() {
		String header = protocolVersion.getVersionString() + " " + statusCode.getStatusCode() + " " + statusCode.getStatusMessage() + "\r\n";
		for(Map.Entry<String, List<String>> f : fields.getFields().entrySet()) {
			for(String v : f.getValue()) {
				header += f.getKey() + ": " + v + "\r\n";
			}
		}
		header += "\r\n";
		byte[] hbs = header.getBytes(StandardCharsets.UTF_8);
		byte[] bs = new byte[hbs.length + content.length];
		System.arraycopy(hbs, 0, bs, 0, hbs.length);
		System.arraycopy(content, 0, bs, hbs.length, content.length);
		return bs;
	}
	
}
