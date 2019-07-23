package me.mrletsplay.webinterfaceapi.http.header;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpClientHeader {

	private String method;
	private HttpRequestPath path;
	private String protocolVersion;
	private HttpHeaderFields fields;
	private byte[] postData;
	
	public HttpClientHeader(String method, HttpRequestPath path, String protocolVersion, HttpHeaderFields fields, byte[] postData) {
		this.method = method;
		this.path = path;
		this.protocolVersion = protocolVersion;
		this.fields = fields;
	}
	
	public String getMethod() {
		return method;
	}
	
	public HttpRequestPath getPath() {
		return path;
	}
	
	public String getProtocolVersion() {
		return protocolVersion;
	}
	
	public HttpHeaderFields getFields() {
		return fields;
	}
	
	public byte[] getPostData() {
		return postData;
	}
	
	public static HttpClientHeader parse(byte[] data) {
		List<String> lines = new ArrayList<>(Arrays.asList(new String(data, StandardCharsets.UTF_8).split("\r\n")));
		if(lines.isEmpty() || lines.stream().allMatch(String::isEmpty)) return null;
		String[] fs = lines.remove(0).split(" ");
		String method = fs[0];
		HttpRequestPath path = HttpRequestPath.parse(fs[1]);
		String protocolVersion = fs[2];
		HttpHeaderFields fields = new HttpHeaderFields();
		String l;
		while(!lines.isEmpty() && (l = lines.remove(0)) != null) {
			if(l.isEmpty()) break;
			String[] kv = l.split(": ", 2);
			fields.addFieldValue(kv[0], kv[1]);
		}
		return new HttpClientHeader(method, path, protocolVersion, fields, null);
	}
	
}
