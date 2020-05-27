package me.mrletsplay.webinterfaceapi.http.header;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.HttpUtils;

public class HttpURLPath {

	private String documentPath;
	private Map<String, List<String>> queryParameters;
	
	public HttpURLPath(String documentPath, Map<String, List<String>> queryParameters) {
		this.documentPath = documentPath;
		this.queryParameters = queryParameters;
	}
	
	public HttpURLPath(String documentPath) {
		this(documentPath, new HashMap<>());
	}
	
	public void setDocumentPath(String path) {
		this.documentPath = path;
	}
	
	public String getDocumentPath() {
		return documentPath;
	}
	
	public boolean hasQueryParameter(String key) {
		return queryParameters.containsKey(key);
	}
	
	public List<String> getQueryParameterValues(String key) {
		return queryParameters.getOrDefault(key, new ArrayList<>());
	}
	
	public String getQueryParameterValue(String key) {
		List<String> ps = getQueryParameterValues(key);
		return ps.isEmpty() ? null : ps.get(0);
	}
	
	public String getQueryParameterValue(String key, String fallback) {
		return hasQueryParameter(key) ? getQueryParameterValue(key) : fallback;
	}
	
	public void setQueryParameterValue(String key, String value) {
		queryParameters.put(key, new ArrayList<>(Arrays.asList(value)));
	}
	
	public void addQueryParameterValue(String key, String value) {
		if(!hasQueryParameter(key)) {
			setQueryParameterValue(key, value);
			return;
		}
		queryParameters.get(key).add(value);
	}
	
	public Map<String, List<String>> getQueryParameters() {
		return queryParameters;
	}
	
	public static HttpURLPath parse(String rawPath) {
		String[] psp = rawPath.split("\\?", 2);
		String path = psp[0];
		Map<String, List<String>> queryParameters = new HashMap<>();
		if(psp.length == 2) queryParameters = parseQueryParameters(psp[1]);
		return new HttpURLPath(path, queryParameters);
	}
	
	public static Map<String, List<String>> parseQueryParameters(String queryParams) {
		Map<String, List<String>> queryParameters = new HashMap<>();
		for(String st : queryParams.split("&")) {
			String[] kv = st.split("=", 2);
			String key = HttpUtils.urlDecode(kv[0]);
			List<String> vs = queryParameters.getOrDefault(key, new ArrayList<>());
			vs.add(kv.length == 2 ? HttpUtils.urlDecode(kv[1]) : "");
			queryParameters.put(key, vs);
		}
		return queryParameters;
	}
	
	public static HttpURLPath of(String path) {
		return parse(path);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof HttpURLPath)) return false;
		HttpURLPath o = (HttpURLPath) obj;
		return documentPath.equals(o.documentPath)
				&& queryParameters.equals(o.queryParameters);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(documentPath, queryParameters);
	}
	
	@Override
	public String toString() {
		String queryString = queryParameters.entrySet().stream()
				.map(en -> en.getValue().stream()
						.map(v -> HttpUtils.urlEncode(en.getKey()) + "=" + HttpUtils.urlEncode(v))
						.collect(Collectors.joining("&")))
				.collect(Collectors.joining("&"));
		return documentPath + (queryString.isEmpty() ? "" : "?" + queryString);
	}
	
}
