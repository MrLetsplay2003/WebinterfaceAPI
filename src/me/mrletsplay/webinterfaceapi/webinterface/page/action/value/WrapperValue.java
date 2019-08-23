package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

import java.util.function.Supplier;

public class WrapperValue implements WebinterfaceActionValue {

	private WebinterfaceActionValue value;
	private Supplier<String> wrapper;
	
	public WrapperValue(WebinterfaceActionValue value, Supplier<String> wrapper) {
		this.value = value;
		this.wrapper = wrapper;
	}
	
	public WrapperValue(WebinterfaceActionValue value, String wrapper) {
		this(value, () -> wrapper);
	}
	
	@Override
	public String toJavaScript() {
		return String.format(wrapper.get(), value.toJavaScript());
	}

}
