package me.mrletsplay.webinterfaceapi.http.header;

import me.mrletsplay.webinterfaceapi.server.ServerException;

public class PostData {

	private String contentType;
	private byte[] content;
	
	public PostData(String contentType, byte[] content) {
		this.contentType = (contentType != null && contentType.contains(";")) ? contentType.substring(0, contentType.indexOf(";")) : contentType; // TODO: extra parameters (e.g. charset)
		this.content = content;
	}
	
	public Object getParsedAs(HttpClientContentType type) {
		return type.parse(content);
	}
	
	public Object getParsed() {
		HttpClientContentTypes t = HttpClientContentTypes.getByMimeType(contentType);
		if(t == null) throw new ServerException("Unknown content type: " + contentType);
		return getParsedAs(t);
	}
	
	public byte[] getRaw() {
		return content;
	}
	
}
