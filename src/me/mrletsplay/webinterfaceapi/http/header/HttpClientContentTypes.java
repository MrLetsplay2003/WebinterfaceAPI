package me.mrletsplay.webinterfaceapi.http.header;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import me.mrletsplay.mrcore.json.JSONParser;

public enum HttpClientContentTypes implements HttpClientContentType {
	
	JSON(c -> JSONParser.parse(new String(c, StandardCharsets.UTF_8)), "application/json"),
	TEXT(c -> new String(c, StandardCharsets.UTF_8), "text/plain"),
	URLENCODED(c -> HttpRequestPath.parseGetParameters(new String(c, StandardCharsets.UTF_8)), "application/x-www-form-urlencoded"),
	// TODO: form multipart
	;
	
	private Function<byte[], Object> parsingFunction;
	private List<String> mimeTypes;
	
	private HttpClientContentTypes(Function<byte[], Object> parsingFunction, String... mimeTypes) {
		this.parsingFunction = parsingFunction;
		this.mimeTypes = Arrays.asList(mimeTypes);
	}
	
	@Override
	public List<String> getMimeTypes() {
		return mimeTypes;
	}
	
	@Override
	public Object parse(byte[] content) {
		return parsingFunction.apply(content);
	}
	
	public static HttpClientContentTypes getByMimeType(String mimeType) {
		return Arrays.stream(values()).filter(t -> t.getMimeTypes().contains(mimeType.toLowerCase())).findFirst().orElse(null);
	}

}
