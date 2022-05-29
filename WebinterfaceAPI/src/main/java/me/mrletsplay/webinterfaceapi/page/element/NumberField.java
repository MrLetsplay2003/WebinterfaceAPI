package me.mrletsplay.webinterfaceapi.page.element;

import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;

public class NumberField extends AbstractPageElement {

	private Supplier<String> placeholder;

	private Supplier<Double> initialValue;

	private int allowedDecimals;

	private Double
		min,
		max;

	private Action onChangeAction;

	public NumberField(Supplier<String> placeholder, Supplier<Double> initialValue) {
		this.placeholder = placeholder;
		this.initialValue = initialValue;
	}

	public NumberField(String placeholder, Double initialValue) {
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

	public void setPlaceholder(Supplier<String> placeholder) {
		this.placeholder = placeholder;
	}

	public void setPlaceholder(String placeholder) {
		setPlaceholder(() -> placeholder);
	}

	public Supplier<String> getPlaceholder() {
		return placeholder;
	}

	public void setInitialValue(Supplier<Double> initialValue) {
		this.initialValue = initialValue;
	}

	public void setInitialValue(Double initialValue) {
		setInitialValue(() -> initialValue);
	}

	public Supplier<Double> getInitialValue() {
		return initialValue;
	}

	public void setAllowedDecimals(int allowedDecimals) {
		this.allowedDecimals = allowedDecimals;
	}

	public int getAllowedDecimals() {
		return allowedDecimals;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMin() {
		return min;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMax() {
		return max;
	}

	public void setOnChangeAction(Action onChangeAction) {
		this.onChangeAction = onChangeAction;
	}

	public ActionValue inputValue() {
		ActionValue v = ActionValue.elementValue(this);
		return () -> (allowedDecimals > 0 ? "parseInt(" : "parseFloat(") + v.toJavaScript() + ")";
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("input");
		b.setSelfClosing(true);
		b.setAttribute("type", "number");
		b.setAttribute("placeholder", placeholder);
		b.setAttribute("aria-label", placeholder);
		b.setAttribute("oninput", "checkInputValidity(this)");

		if(min != null) b.setAttribute("min", min.toString());
		if(max != null) b.setAttribute("max", max.toString());
		if(allowedDecimals != 0) b.setAttribute("step", String.valueOf(Math.pow(10, -allowedDecimals)));

		if(initialValue != null) {
			Double v = initialValue.get();
			if(v != null) {
				Number val = v == v.longValue() ? (Number) v.longValue() : (Number) v;
				b.setAttribute("value", String.valueOf(val));
			}
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

		public Builder initialValue(Double initialValue) {
			element.setInitialValue(initialValue);
			return this;
		}

		public Builder initialValue(Supplier<Double> initialValue) {
			element.setInitialValue(initialValue);
			return this;
		}

		public Builder allowedDecimals(int allowedDecimals) {
			element.setAllowedDecimals(allowedDecimals);
			return this;
		}

		public Builder min(Double min) {
			element.setMin(min);
			return this;
		}

		public Builder max(Double max) {
			element.setMax(max);
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
