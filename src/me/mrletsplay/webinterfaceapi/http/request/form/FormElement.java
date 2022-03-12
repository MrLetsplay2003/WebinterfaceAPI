package me.mrletsplay.webinterfaceapi.http.request.form;

import java.util.Map;

import me.mrletsplay.webinterfaceapi.http.header.HttpHeaderFields;

public class FormElement {

	private HttpHeaderFields headers;
	private byte[] data;
	private Map<String, String> properties;
	
	public FormElement(HttpHeaderFields headers, byte[] data, Map<String, String> properties) {
		this.headers = headers;
		this.data = data;
		this.properties = properties;
	}
	
	public HttpHeaderFields getHeaders() {
		return headers;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
}
