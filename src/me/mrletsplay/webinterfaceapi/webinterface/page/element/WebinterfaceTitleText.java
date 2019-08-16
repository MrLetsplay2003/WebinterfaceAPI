package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceTitleText extends AbstractWebinterfacePageElement {
	
	private Supplier<String> text;
	
	public WebinterfaceTitleText(Supplier<String> text) {
		this.text = text;
	}
	
	public WebinterfaceTitleText(String text) {
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
		HtmlElement b = new HtmlElement("b");
		b.setText(text);
		return b;
	}

}
