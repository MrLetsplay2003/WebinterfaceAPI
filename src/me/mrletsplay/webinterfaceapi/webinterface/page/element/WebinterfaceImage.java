package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlImg;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceImage extends AbstractWebinterfacePageElement {
	
	private Supplier<String>
		src,
		alt;
	
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
	
	private WebinterfaceImage() {}
	
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
	
	public static Builder builder() {
		return new Builder(new WebinterfaceImage());
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceImage, Builder> {

		private Builder(WebinterfaceImage element) {
			super(element);
		}
		
		public Builder src(String src) {
			element.setSource(src);
			return this;
		}
		
		public Builder src(Supplier<String> src) {
			element.setSource(src);
			return this;
		}
		
		public Builder alt(String alt) {
			element.setAlt(alt);
			return this;
		}
		
		public Builder alt(Supplier<String> alt) {
			element.setAlt(alt);
			return this;
		}
		
		@Override
		public WebinterfaceImage create() throws IllegalStateException {
			if(element.getSource() == null) throw new IllegalStateException("No source set");
			return super.create();
		}
		
	}

}
