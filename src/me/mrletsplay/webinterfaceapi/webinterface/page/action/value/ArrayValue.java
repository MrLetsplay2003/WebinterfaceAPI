package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayValue implements WebinterfaceActionValue {

	private List<WebinterfaceActionValue> values;
	
	public ArrayValue(List<WebinterfaceActionValue> values) {
		this.values = values;
	}
	
	public ArrayValue(WebinterfaceActionValue... values) {
		this(new ArrayList<>(Arrays.asList(values)));
	}
	
	public void add(WebinterfaceActionValue value) {
		values.add(value);
	}
	
	@Deprecated
	public void addValue(WebinterfaceActionValue value) {
		add(value);
	}
	
	@Override
	public String toJavaScript() {
		return "[" + values.stream()
			.map(v -> v.toJavaScript())
			.collect(Collectors.joining(",")) + "]";
	}

}
