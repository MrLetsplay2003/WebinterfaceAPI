package me.mrletsplay.webinterfaceapi.page.action.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayValue implements ActionValue {

	private List<ActionValue> values;

	protected ArrayValue(List<ActionValue> values) {
		this.values = new ArrayList<>(values);
	}

	protected ArrayValue(ActionValue... values) {
		this(new ArrayList<>(Arrays.asList(values)));
	}

	public ArrayValue add(ActionValue value) {
		values.add(value);
		return this;
	}

	@Override
	public String toJavaScript() {
		return "[" + values.stream()
			.map(v -> v.toJavaScript())
			.collect(Collectors.joining(",")) + "]";
	}

}
