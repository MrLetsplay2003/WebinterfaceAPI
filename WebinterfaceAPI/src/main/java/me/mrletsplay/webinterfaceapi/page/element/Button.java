package me.mrletsplay.webinterfaceapi.page.element;

import java.util.function.Supplier;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.dom.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class Button extends AbstractPageElement {

	private Supplier<String> text;
	private String icon;

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

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	@Override
	public HtmlElement createElement() {
		HtmlButton b = HtmlElement.button();
		b.setText(text);
		if(icon != null) b.appendChild(WebinterfaceUtils.iconifyIcon(icon));
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

		public Builder icon(String icon) {
			element.setIcon(icon);
			return this;
		}

		@Override
		public Button create() throws IllegalStateException {
			if(element.getText() == null && element.getIcon() == null) throw new IllegalStateException("No text or icon set");
			return super.create();
		}

	}

}
