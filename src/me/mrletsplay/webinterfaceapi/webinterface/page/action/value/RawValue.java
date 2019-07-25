package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

import java.util.function.Supplier;

public class RawValue implements WebinterfaceActionValue {

	private Supplier<String> value;
	
	public RawValue(Supplier<String> value) {
		this.value = value;
	}
	
	public RawValue(String value) {
		this(() -> value);
	}
	
	@Override
	public String toJavaScript() {
		return value.get();
	}

}
