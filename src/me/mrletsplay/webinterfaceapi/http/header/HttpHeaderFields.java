package me.mrletsplay.webinterfaceapi.http.header;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpHeaderFields {

	private Map<String, List<String>> fields;
	
	public HttpHeaderFields() {
		this.fields = new HashMap<>();
	}
	
	public void setFieldValue(String name, String value) {
		fields.remove(name.toLowerCase());
		fields.put(name.toLowerCase(), new ArrayList<>(Arrays.asList(value)));
	}
	
	public void addFieldValue(String name, String value) {
		List<String> vs = getFieldValues(name.toLowerCase());
		vs.add(value);
		fields.put(name.toLowerCase(), vs);
	}
	
	public List<String> getFieldValues(String name) {
		return fields.getOrDefault(name.toLowerCase(), new ArrayList<>());
	}
	
	public String getFieldValue(String name) {
		List<String> vs = getFieldValues(name.toLowerCase());
		return vs.isEmpty() ? null : vs.get(0);
	}
	
	public Map<String, String> getCookies() {
		Map<String, String> cookies = new HashMap<>();
		for(String c : getFieldValues("Cookie")) {
			for(String co : c.split("; ")) {
				String[] kv = co.split("=");
				cookies.put(kv[0], kv[1]);
			}
		}
		return cookies;
	}
	
	public String getCookie(String name) {
		return getCookies().get(name);
	}
	
	public void setCookie(String name, String value, String... properties) {
		addFieldValue("Set-Cookie", name + "=" + value + (properties.length == 0 ? "" : "; " + Arrays.stream(properties).collect(Collectors.joining("; "))));
	}
	
	public Map<String, List<String>> getFields() {
		return fields;
	}
	
}
