package me.mrletsplay.webinterfaceapi.http.header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.mrletsplay.mrcore.http.HttpUtils;

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
		if(psp.length == 2) getParameters = parseGetParameters(psp[1]);
		return new HttpRequestPath(path, getParameters);
	}
	
	public static Map<String, List<String>> parseGetParameters(String getParams) {
		Map<String, List<String>> getParameters = new HashMap<>();
		for(String st : getParams.split("&")) {
			String[] kv = st.split("=", 2);
			String key = HttpUtils.urlDecode(kv[0]);
			List<String> vs = getParameters.getOrDefault(key, new ArrayList<>());
			vs.add(kv.length == 2 ? HttpUtils.urlDecode(kv[1]) : "");
			getParameters.put(key, vs);
		}
		return getParameters;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof HttpRequestPath)) return false;
		HttpRequestPath o = (HttpRequestPath) obj;
		return path.equals(o.path)
				&& getParameters.equals(o.getParameters);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(path, getParameters);
	}
	
}
