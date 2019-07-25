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
		this.postData = postData;
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
	
	public PostData getPostData() {
		return new PostData(fields.getFieldValue("Content-Type"), postData);
	}
	
	public static HttpClientHeader parse(byte[] data) {
		if(data.length == 0) return null;
		String dt = new String(data, StandardCharsets.UTF_8);
		dt = dt.substring(0, dt.indexOf("\r\n\r\n"));
		List<String> lines = new ArrayList<>(Arrays.asList(dt.split("\r\n")));
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
		int prDtLen = dt.getBytes(StandardCharsets.UTF_8).length;
		byte[] postData = new byte[data.length - prDtLen - 4]; // - 4 bytes because of the \r\n\r\n
		System.arraycopy(data, prDtLen + 4, postData, 0, postData.length);
		return new HttpClientHeader(method, path, protocolVersion, fields, postData);
	}
	
}
