package me.mrletsplay.webinterfaceapi.page.element;

import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;

public class PasswordField extends AbstractPageElement {

	private Supplier<String>
		placeholder;

	private Supplier<Boolean>
		showInitialValue;

	private Integer
		minLength,
		maxLength;

	private Action onChangeAction;

	public PasswordField(Supplier<String> placeholder, Supplier<Boolean> showInitialValue) {
		this.placeholder = placeholder;
		this.showInitialValue = showInitialValue;
	}

	public PasswordField(String placeholder, boolean showInitialValue) {
		this(() -> placeholder, () -> showInitialValue);
	}

	public PasswordField(Supplier<String> placeholder) {
		this(placeholder, () -> false);
	}

	public PasswordField(String placeholder) {
		this(() -> placeholder);
	}

	public PasswordField() {
		this(() -> "Password");
	}

	public void setPlaceholder(Supplier<String> placeholder) {
		this.placeholder = placeholder;
	}

	public void setPlaceholder(String placeholder) {
		setPlaceholder(() -> placeholder);
	}

	public Supplier<String> getPlaceholder() {
		return placeholder;
	}

	public void setShowInitialValue(Supplier<Boolean> showInitialValue) {
		this.showInitialValue = showInitialValue;
	}

	public void setShowInitialValue(boolean showInitialValue) {
		setShowInitialValue(() -> showInitialValue);
	}

	public Supplier<Boolean> getShowInitialValue() {
		return showInitialValue;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setOnChangeAction(Action onChangeAction) {
		this.onChangeAction = onChangeAction;
	}

	public ActionValue inputValue() {
		return ActionValue.elementValue(this);
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("input");
		b.setSelfClosing(true);
		b.setAttribute("type", "password");
		b.setAttribute("placeholder", placeholder);
		b.setAttribute("aria-label", placeholder);
		b.setAttribute("oninput", "checkInputValidity(this)");

		if(minLength != null) b.setAttribute("minlength", String.valueOf(minLength));
		if(maxLength != null) b.setAttribute("maxlength", String.valueOf(maxLength));

		if(showInitialValue != null) {
			Boolean v = showInitialValue.get();
			if(v != null && v) b.setAttribute("value", "censored");
		}
		if(onChangeAction != null) b.setAttribute("onchange", onChangeAction.createAttributeValue());
		return b;
	}

	public static Builder builder() {
		return new Builder(new PasswordField());
	}

	public static class Builder extends AbstractElementBuilder<PasswordField, Builder> {

		private Builder(PasswordField element) {
			super(element);
		}

		public Builder placeholder(String placeholder) {
			element.setPlaceholder(placeholder);
			return this;
		}

		public Builder placeholder(Supplier<String> placeholder) {
			element.setPlaceholder(placeholder);
			return this;
		}

		public Builder showInitialValue(boolean showInitialValue) {
			element.setShowInitialValue(showInitialValue);
			return this;
		}

		public Builder showInitialValue(Supplier<Boolean> showInitialValue) {
			element.setShowInitialValue(showInitialValue);
			return this;
		}

		public Builder minLength(Integer minLength) {
			element.setMinLength(minLength);
			return this;
		}

		public Builder maxLength(Integer maxLength) {
			element.setMaxLength(maxLength);
			return this;
		}

		public Builder onChange(Action onChange) {
			element.setOnChangeAction(onChange);
			return this;
		}

		public Builder onChange(Function<PasswordField, Action> onChange) {
			element.setOnChangeAction(onChange.apply(element));
			return this;
		}

		@Override
		public PasswordField create() throws IllegalStateException {
			return super.create();
		}

	}

}
