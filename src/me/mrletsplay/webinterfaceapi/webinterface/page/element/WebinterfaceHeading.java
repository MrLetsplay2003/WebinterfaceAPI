package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

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
	
	private WebinterfaceHeading() {}
	
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
	
	public static Builder builder() {
		return new Builder(new WebinterfaceHeading());
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceHeading, Builder> {

		private Builder(WebinterfaceHeading element) {
			super(element);
		}
		
		public Builder text(String text) {
			element.setText(text);
			return this;
		}
		
		public Builder text(Supplier<String> text) {
			element.setText(text);
			return this;
		}
		
		public Builder level(int level) {
			element.setLevel(level);
			return this;
		}
		
		@Override
		public WebinterfaceHeading create() throws IllegalStateException {
			if(element.getText() == null) throw new IllegalStateException("No text set");
			return super.create();
		}
		
	}

}
