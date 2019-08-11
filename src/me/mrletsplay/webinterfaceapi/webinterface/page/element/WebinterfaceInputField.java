package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceInputField extends AbstractWebinterfacePageElement {
	
	private Supplier<String> placeholder;
	
	public WebinterfaceInputField() {
		this(() -> "Text");
	}
	
	public WebinterfaceInputField(Supplier<String> placeholder) {
		this.placeholder = placeholder;
	}
	
	public WebinterfaceInputField(String placeholder) {
		this(() -> placeholder);
	}
	
	public void setPlaceholder(Supplier<String> placeholder) {
		this.placeholder = placeholder;
	}
	
	public void setPlaceholder(String placeholder) {
		setPlaceholder(() -> placeholder);
	}
	
	public Supplier<String> getPlaceholder() {
		return placeholder;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("input");
		b.setAttribute("type", "text");
		b.setAttribute("placeholder", placeholder);
		return b;
	}

}
