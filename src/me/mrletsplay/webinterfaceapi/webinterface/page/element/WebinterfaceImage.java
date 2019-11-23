package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlImg;

public class WebinterfaceImage extends AbstractWebinterfacePageElement {
	
	private Supplier<String> src;
	
	public WebinterfaceImage(Supplier<String> src) {
		this.src = src;
	}
	
	public WebinterfaceImage(String src) {
		this(() -> src);
	}
	
	public void setSource(Supplier<String> src) {
		this.src = src;
	}
	
	public void setSource(String src) {
		setSource(() -> src);
	}
	
	public Supplier<String> setSource() {
		return src;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlImg img = HtmlElement.img(src, () -> "lol");
		return img;
	}

}
