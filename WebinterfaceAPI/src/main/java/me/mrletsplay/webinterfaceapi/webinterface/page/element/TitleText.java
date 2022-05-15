package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class TitleText extends AbstractPageElement {

	private Supplier<String> text;

	public TitleText(Supplier<String> text) {
		this.text = text;
	}

	public TitleText(String text) {
		this(() -> text);
	}

	private TitleText() {}

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
		return new Builder(new TitleText());
	}

	public static class Builder extends AbstractElementBuilder<TitleText, Builder> {

		private Builder(TitleText element) {
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
		public TitleText create() throws IllegalStateException {
			if(element.getText() == null) throw new IllegalStateException("No text set");
			return super.create();
		}

	}

}
