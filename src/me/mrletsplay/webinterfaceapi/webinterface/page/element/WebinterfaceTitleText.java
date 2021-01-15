package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceTitleText extends AbstractWebinterfacePageElement {
	
	private Supplier<String> text;
	
	public WebinterfaceTitleText(Supplier<String> text) {
		this.text = text;
	}
	
	public WebinterfaceTitleText(String text) {
		this(() -> text);
	}
	
	private WebinterfaceTitleText() {}
	
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
	
	public static Builder builder() {
		return new Builder(new WebinterfaceTitleText());
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceTitleText, Builder> {

		private Builder(WebinterfaceTitleText element) {
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
		
		public Builder noLineBreaks() {
			element.getStyle().setProperty("white-space", "nowrap");
			return this;
		}
		
		@Override
		public WebinterfaceTitleText create() throws IllegalStateException {
			if(element.getText() == null) throw new IllegalStateException("No text set");
			return super.create();
		}
		
	}

}
