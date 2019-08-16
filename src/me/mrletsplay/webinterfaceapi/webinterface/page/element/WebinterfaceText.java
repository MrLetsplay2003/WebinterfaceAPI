package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceText extends AbstractWebinterfacePageElement {
	
	private Supplier<String> text;
	
	public WebinterfaceText(Supplier<String> text) {
		this.text = text;
	}
	
	public WebinterfaceText(String text) {
		this(() -> text);
	}
	
	public void setText(Supplier<String> text) {
		this.text = text;
	}
	
	public void setText(String text) {
		setText(() -> text);
	}
	
	public Supplier<String> getText() {
		return text;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("a");
		b.setText(text);
		return b;
	}

}
