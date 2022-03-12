package me.mrletsplay.webinterfaceapi.http.header;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import me.mrletsplay.mrcore.json.JSONParser;
import me.mrletsplay.webinterfaceapi.http.request.Multipart;

public enum HttpClientContentTypes implements HttpClientContentType {
	
	JSON((h, c) -> JSONParser.parse(new String(c, StandardCharsets.UTF_8)), "application/json"),
	TEXT((h, c) -> new String(c, StandardCharsets.UTF_8), "text/plain"), // TODO: extra parameters (e.g. charset)
	URLENCODED((h, c) -> HttpURLPath.parseQueryParameters(new String(c, StandardCharsets.UTF_8)), "application/x-www-form-urlencoded"),
	MULTIPART((h, c) -> Multipart.parse(h, c), "multipart/form-data", "multipart/mixed"),
	OCTET_STREAM((h, c) -> c, "application/octet-stream"),
	;
	
	private BiFunction<HttpHeaderFields, byte[], Object> parsingFunction;
	private List<String> mimeTypes;
	
	private HttpClientContentTypes(BiFunction<HttpHeaderFields, byte[], Object> parsingFunction, String... mimeTypes) {
		this.parsingFunction = parsingFunction;
		this.mimeTypes = Arrays.asList(mimeTypes);
	}
	
	@Override
	public List<String> getMimeTypes() {
		return mimeTypes;
	}
	
	@Override
	public Object parse(HttpHeaderFields headers, byte[] content) {
		return parsingFunction.apply(headers, content);
	}
	
	public static HttpClientContentTypes getByMimeType(String mimeType) {
		return Arrays.stream(values())
				.filter(t -> t.getMimeTypes().contains(mimeType.toLowerCase()))
				.findFirst().orElse(null);
	}

}
