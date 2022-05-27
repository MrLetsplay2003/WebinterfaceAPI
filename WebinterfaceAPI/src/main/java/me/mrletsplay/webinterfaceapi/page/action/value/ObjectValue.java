package me.mrletsplay.webinterfaceapi.page.action.value;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

public class ObjectValue implements ActionValue {

	private Map<String, ActionValue> values;

	protected ObjectValue(Map<String, ActionValue> values) {
		this.values = new HashMap<>(values);
	}

	protected ObjectValue() {
		this(new HashMap<>());
	}

	public ObjectValue put(String key, ActionValue value) {
		values.put(key, value);
		return this;
	}

	@Override
	public String toJavaScript() {
		return "{" + values.entrySet().stream()
			.map(v -> "\"" + StringEscapeUtils.escapeEcmaScript(v.getKey()) + "\":" + (v.getValue() == null ? null : v.getValue().toJavaScript()))
			.collect(Collectors.joining(",")) + "}";
	}

}
