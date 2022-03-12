package me.mrletsplay.webinterfaceapi.http.header;

import me.mrletsplay.webinterfaceapi.server.ServerException;

public class PostData {

	private HttpHeaderFields headers;
	private byte[] content;
	
	public PostData(HttpHeaderFields headers, byte[] content) {
		this.headers = headers;
		this.content = content;
	}
	
	public Object getParsedAs(HttpClientContentType type) {
		return type.parse(headers, content);
	}
	
	public Object getParsed() {
		String contentType = headers.getFieldValue("Content-Type");
		contentType = (contentType != null && contentType.contains(";")) ? contentType.substring(0, contentType.indexOf(";")) : contentType;
		HttpClientContentTypes t = HttpClientContentTypes.getByMimeType(contentType);
		if(t == null) throw new ServerException("Unknown or unsupported content type: " + contentType);
		return getParsedAs(t);
	}
	
	public byte[] getRaw() {
		return content;
	}
	
}
