package me.mrletsplay.webinterfaceapi.html.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class HtmlImg extends HtmlElement {
	
	public HtmlImg() {
		super("img");
		setSelfClosing(true);
	}
	
	public void setSrc(Supplier<String> src) {
		setAttribute("src", src);
	}
	
	public void setSrc(String src) {
		setAttribute("src", src);
	}
	
	public Supplier<String> getSrc() {
		return getAttribute("src");
	}
	
}
