package me.mrletsplay.webinterfaceapi.http.header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestPath {

	private String path;
	private Map<String, List<String>> getParameters;
	
	public HttpRequestPath(String path, Map<String, List<String>> getParameters) {
		this.path = path;
		this.getParameters = getParameters;
	}
	
	public String getDocumentPath() {
		return path;
	}
	
	public List<String> getGetParameterValues(String key) {
		return getParameters.getOrDefault(key, new ArrayList<>());
	}
	
	public String getGetParameterValue(String key) {
		List<String> ps = getGetParameterValues(key);
		return ps.isEmpty() ? null : ps.get(0);
	}
	
	public Map<String, List<String>> getGetParameters() {
		return getParameters;
	}
	
	public static HttpRequestPath parse(String rawPath) {
		String[] psp = rawPath.split("\\?", 2);
		String path = psp[0];
		Map<String, List<String>> getParameters = new HashMap<>();
		if(psp.length == 2) {
			for(String st : psp[1].split("&")) {
				String[] kv = st.split("=", 2);
				List<String> vs = getParameters.getOrDefault(kv[0], new ArrayList<>());
				vs.add(kv[1]);
				getParameters.put(kv[0], vs);
			}
		}
		return new HttpRequestPath(path, getParameters);
	}
	
}
