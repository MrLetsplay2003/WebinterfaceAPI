package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfaceHeading extends AbstractWebinterfacePageElement {
	
	private Supplier<String> text;
	private int level;
	
	public WebinterfaceHeading(Supplier<String> text) {
		this.text = text;
		this.level = 1;
	}
	
	public WebinterfaceHeading(String text) {
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
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement h = new HtmlElement("h" + level);
		HtmlElement b = new HtmlElement("b");
		b.setText(text);
		h.appendChild(b);
		return h;
	}

}
