package me.mrletsplay.webinterfaceapi.page.element;

import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;

public class InputField extends AbstractPageElement {

	private Supplier<String>
		placeholder,
		initialValue;

	private Integer
		minLength,
		maxLength;

	private Action onChangeAction;

	public InputField(Supplier<String> placeholder, Supplier<String> initialValue) {
		this.placeholder = placeholder;
		this.initialValue = initialValue;
	}

	public InputField(String placeholder, String initialValue) {
		this(() -> placeholder, () -> initialValue);
	}

	public InputField(Supplier<String> placeholder) {
		this(placeholder, null);
	}

	public InputField(String placeholder) {
		this(() -> placeholder);
	}

	public InputField() {
		this(() -> "Text");
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

	public void setInitialValue(Supplier<String> initialValue) {
		this.initialValue = initialValue;
	}

	public void setInitialValue(String initialValue) {
		setInitialValue(() -> initialValue);
	}

	public Supplier<String> getInitialValue() {
		return initialValue;
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
		b.setAttribute("type", "text");
		b.setAttribute("placeholder", placeholder);
		b.setAttribute("aria-label", placeholder);
		b.setAttribute("oninput", "checkInputValidity(this)");

		if(minLength != null) b.setAttribute("minlength", String.valueOf(minLength));
		if(maxLength != null) b.setAttribute("maxlength", String.valueOf(maxLength));

		if(initialValue != null) {
			String v = initialValue.get();
			if(v != null) b.setAttribute("value", v);
		}
		if(onChangeAction != null) b.setAttribute("onchange", onChangeAction.createAttributeValue());
		return b;
	}

	public static Builder builder() {
		return new Builder(new InputField());
	}

	public static class Builder extends AbstractElementBuilder<InputField, Builder> {

		private Builder(InputField element) {
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

		public Builder initialValue(String placeholder) {
			element.setInitialValue(placeholder);
			return this;
		}

		public Builder initialValue(Supplier<String> placeholder) {
			element.setInitialValue(placeholder);
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

		public Builder onChange(Function<InputField, Action> onChange) {
			element.setOnChangeAction(onChange.apply(element));
			return this;
		}

		@Override
		public InputField create() throws IllegalStateException {
			return super.create();
		}

	}

}
