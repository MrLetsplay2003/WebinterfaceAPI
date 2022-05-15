package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.dom.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class Button extends AbstractPageElement {

	private Supplier<String> text;

	public Button(Supplier<String> text) {
		this.text = text;
	}

	public Button(String text) {
		this(() -> text);
	}

	private Button() {}

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
		return new Builder(new Button());
	}

	public static class Builder extends AbstractElementBuilder<Button, Builder> {

		private Builder(Button element) {
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
		public Button create() throws IllegalStateException {
			if(element.getText() == null) throw new IllegalStateException("No text set");
			return super.create();
		}

	}

}
