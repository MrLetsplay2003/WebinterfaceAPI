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
	
	public StringValue concat(StringValue other) {
		Supplier<String> oldV = value;
		this.value = () -> oldV.get() + other.value.get();
		return this;
	}
	
	public RawValue concat(WebinterfaceActionValue other) {
		return new RawValue(toJavaScript() + " + " + other.toJavaScript());
	}
	
	@Override
	public String toJavaScript() {
		String v = value.get();
		return v == null ? "null" : "\"" + StringEscapeUtils.escapeEcmaScript(v) + "\"";
	}

}
