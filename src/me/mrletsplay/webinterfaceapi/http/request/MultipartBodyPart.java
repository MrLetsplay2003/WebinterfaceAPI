package me.mrletsplay.webinterfaceapi.http.request;

import me.mrletsplay.webinterfaceapi.http.header.HttpHeaderFields;

public class MultipartBodyPart {
	
	private HttpHeaderFields headers;
	private byte[] data;
	
	public MultipartBodyPart(HttpHeaderFields headers, byte[] data) {
		this.headers = headers;
		this.data = data;
	}
	
	public HttpHeaderFields getHeaders() {
		return headers;
	}
	
	public byte[] getData() {
		return data;
	}
	
}
