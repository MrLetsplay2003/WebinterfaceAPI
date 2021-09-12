package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceButton extends AbstractWebinterfacePageElement {
	
	private Supplier<String> text;
	
	public WebinterfaceButton(Supplier<String> text) {
		this.text = text;
	}
	
	public WebinterfaceButton(String text) {
		this(() -> text);
	}
	
	private WebinterfaceButton() {}
	
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
		HtmlButton b = HtmlElement.button();
		b.setText(text);
		return b;
	}
	
	public static Builder builder() {
		return new Builder(new WebinterfaceButton());
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceButton, Builder> {

		private Builder(WebinterfaceButton element) {
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
		
		@Override
		public WebinterfaceButton create() throws IllegalStateException {
			if(element.getText() == null) throw new IllegalStateException("No text set");
			return super.create();
		}
		
	}

}
