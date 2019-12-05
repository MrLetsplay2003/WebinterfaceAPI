package me.mrletsplay.webinterfaceapi.http.header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.HttpUtils;

public class HttpRequestPath {

	private String path;
	private Map<String, List<String>> queryParameters;
	
	public HttpRequestPath(String path, Map<String, List<String>> queryParameters) {
		this.path = path;
		this.queryParameters = queryParameters;
	}
	
	public String getDocumentPath() {
		return path;
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
	
	public Map<String, List<String>> getQueryParameters() {
		return queryParameters;
	}
	
	public static HttpRequestPath parse(String rawPath) {
		String[] psp = rawPath.split("\\?", 2);
		String path = psp[0];
		Map<String, List<String>> queryParameters = new HashMap<>();
		if(psp.length == 2) queryParameters = parseQueryParameters(psp[1]);
		return new HttpRequestPath(path, queryParameters);
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
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof HttpRequestPath)) return false;
		HttpRequestPath o = (HttpRequestPath) obj;
		return path.equals(o.path)
				&& queryParameters.equals(o.queryParameters);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(path, queryParameters);
	}
	
	@Override
	public String toString() {
		String queryString = queryParameters.entrySet().stream()
				.map(en -> en.getValue().stream()
						.map(v -> HttpUtils.urlEncode(en.getKey()) + "=" + HttpUtils.urlEncode(v))
						.collect(Collectors.joining("&")))
				.collect(Collectors.joining("&"));
		return path + (queryString.isEmpty() ? "" : "?" + queryString);
	}
	
}
