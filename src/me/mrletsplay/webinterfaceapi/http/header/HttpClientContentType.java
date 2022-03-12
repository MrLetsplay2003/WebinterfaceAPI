package me.mrletsplay.webinterfaceapi.http.header;

import java.util.List;

public interface HttpClientContentType {
	
	public List<String> getMimeTypes();

	public Object parse(HttpHeaderFields headers, byte[] content);
	
}
