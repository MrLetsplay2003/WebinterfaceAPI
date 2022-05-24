package me.mrletsplay.webinterfaceapi.page.element;

import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;

public class NumberField extends AbstractPageElement {

	private boolean allowFloats;

	private Supplier<String>
		placeholder,
		initialValue;

	private Action onChangeAction;

	public NumberField(Supplier<String> placeholder, Supplier<String> initialValue) {
		this.placeholder = placeholder;
		this.initialValue = initialValue;
	}

	public NumberField(String placeholder, String initialValue) {
		this(() -> placeholder, () -> initialValue);
	}

	public NumberField(Supplier<String> placeholder) {
		this(placeholder, null);
	}

	public NumberField(String placeholder) {
		this(() -> placeholder);
	}

	public NumberField() {
		this(() -> "Text");
	}

	public void setAllowFloats(boolean allowFloats) {
		this.allowFloats = allowFloats;
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

	public void setOnChangeAction(Action onChangeAction) {
		this.onChangeAction = onChangeAction;
	}

	public ActionValue inputValue() {
		ActionValue v = ActionValue.elementValue(this);
		return () -> (allowFloats ? "parseInt(" : "parseFloat(") + v.toJavaScript() + ")";
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("input");
		b.setSelfClosing(true);
		b.setAttribute("type", "number");
		b.setAttribute("placeholder", placeholder);
		b.setAttribute("aria-label", placeholder);
		if(initialValue != null) {
			String v = initialValue.get();
			if(v != null) b.setAttribute("value", v);
		}
		if(onChangeAction != null) b.setAttribute("onchange", onChangeAction.createAttributeValue());
		return b;
	}

	public static Builder builder() {
		return new Builder(new NumberField());
	}

	public static class Builder extends AbstractElementBuilder<NumberField, Builder> {

		private Builder(NumberField element) {
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

		public Builder onChange(Action onChange) {
			element.setOnChangeAction(onChange);
			return this;
		}

		public Builder onChange(Function<NumberField, Action> onChange) {
			element.setOnChangeAction(onChange.apply(element));
			return this;
		}

		@Override
		public NumberField create() throws IllegalStateException {
			return super.create();
		}

	}

}
