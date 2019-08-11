package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

public class ObjectValue implements WebinterfaceActionValue {

	private Map<String, WebinterfaceActionValue> values;
	
	public ObjectValue(Map<String, WebinterfaceActionValue> values) {
		this.values = values;
	}
	
	public ObjectValue() {
		this(new HashMap<>());
	}
	
	public void putValue(String key, WebinterfaceActionValue value) {
		values.put(key, value);
	}
	
	@Override
	public String toJavaScript() {
		return "{" + values.entrySet().stream()
					.map(v -> "\"" + StringEscapeUtils.escapeEcmaScript(v.getKey()) + "\":" + v.getValue().toJavaScript())
					.collect(Collectors.joining(",")) + "}";
	}

}
