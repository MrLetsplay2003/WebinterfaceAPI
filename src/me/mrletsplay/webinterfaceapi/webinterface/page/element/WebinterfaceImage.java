package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlImg;

public class WebinterfaceImage extends AbstractWebinterfacePageElement {
	
	private Supplier<String> src, alt;
	
	public WebinterfaceImage(Supplier<String> src, Supplier<String> alt) {
		this.src = src;
		this.alt = alt;
	}
	
	public WebinterfaceImage(String src, String alt) {
		this(() -> src, () -> alt);
	}
	
	public WebinterfaceImage(Supplier<String> src) {
		this(src, () -> "image");
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
	
	public Supplier<String> getSource() {
		return src;
	}
	
	public void setAlt(Supplier<String> alt) {
		this.alt = alt;
	}
	
	public void setAlt(String alt) {
		setAlt(() -> alt);
	}
	
	public Supplier<String> getAlt() {
		return alt;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlImg img = HtmlElement.img(src, alt);
		return img;
	}

}
