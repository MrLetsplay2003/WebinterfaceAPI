package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

import java.util.function.Supplier;

import org.apache.commons.text.StringEscapeUtils;

public class StringValue implements WebinterfaceActionValue {

	private Supplier<String> value;
	
	public StringValue(Supplier<String> value) {
		this.value = value;
	}
	
	public StringValue(String value) {
		this(() -> value);
	}
	
	public void concat(StringValue other) {
		Supplier<String> oldV = value;
		this.value = () -> oldV.get() + other.value.get();
	}
	
	@Override
	public String toJavaScript() {
		return "\"" + StringEscapeUtils.escapeEcmaScript(value.get()) + "\"";
	}

}
