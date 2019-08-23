package me.mrletsplay.webinterfaceapi.webinterface.page.action.value;

public class AsIntValue extends WrapperValue {

	public AsIntValue(WebinterfaceActionValue value) {
		super(value, "parseInt(%s)");
	}
	
}
