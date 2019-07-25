package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayValue implements WebinterfaceActionValue {

	private List<WebinterfaceActionValue> values;
	
	public ArrayValue(List<WebinterfaceActionValue> values) {
		this.values = values;
	}
	
	public ArrayValue(WebinterfaceActionValue... values) {
		this(Arrays.asList(values));
	}
	
	@Override
	public String toJavaScript() {
		return "[" + values.stream().map(v -> v.toJavaScript()).collect(Collectors.joining(",")) + "]";
	}

}
