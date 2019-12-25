package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlButton;

public class WebinterfaceButton extends AbstractWebinterfacePageElement {
	
	private Supplier<String> text;
	
	public WebinterfaceButton(Supplier<String> text) {
		this.text = text;
	}
	
	public WebinterfaceButton(String text) {
		this(() -> text);
	}
	
	public void setText(Supplier<String> text) {
		this.text = text;
	}
	
	public void setText(String text) {
		setText(() -> text);
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlButton b = HtmlElement.button();
		b.setText(text);
		return b;
	}

}
