package me.mrletsplay.webinterfaceapi.page.element;

import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;

public class CheckBox extends AbstractPageElement {

	private Action onChangeAction;
	private Supplier<Boolean> initialState;
	private Supplier<Boolean> disabled;

	public CheckBox(Supplier<Boolean> initialState) {
		this.initialState = initialState;
	}

	public CheckBox(boolean initialState) {
		this(() -> initialState);
	}

	public CheckBox() {
		this(() -> false);
	}

	public void setInitialState(Supplier<Boolean> initialState) {
		this.initialState = initialState;
	}

	public void setInitialState(boolean initialState) {
		setInitialState(() -> initialState);
	}

	public void setDisabled(Supplier<Boolean> disabled) {
		this.disabled = disabled;
	}

	public void setDisabled(boolean disabled) {
		setDisabled(() -> disabled);
	}

	public void setOnChangeAction(Action onChangeAction) {
		this.onChangeAction = onChangeAction;
	}

	public Supplier<Boolean> getInitialState() {
		return initialState;
	}

	public ActionValue checkedValue() {
		return ActionValue.checkboxValue(this);
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement label = new HtmlElement("label");
		label.addClass("checkbox-container");
		HtmlElement ch = new HtmlElement("input");
		ch.setSelfClosing(true);
		ch.setAttribute("type", "checkbox");
		ch.setAttribute("aria-label", "Yes/No"); // TODO aria-label
		if(initialState.get()) ch.setAttribute("checked");
		if(onChangeAction != null) ch.setAttribute("onchange", onChangeAction.createAttributeValue());
		if(disabled != null && disabled.get()) ch.setAttribute("disabled");
		label.appendChild(ch);
		HtmlElement sp = new HtmlElement("span");
		sp.addClass("checkbox-checkmark");
		label.appendChild(sp);
		return label;
	}

	public static Builder builder() {
		return new Builder(new CheckBox());
	}

	public static class Builder extends AbstractElementBuilder<CheckBox, Builder> {

		private Builder(CheckBox element) {
			super(element);
		}

		public Builder initialState(Supplier<Boolean> initialState) {
			element.setInitialState(initialState);
			return this;
		}

		public Builder initialState(boolean initialState) {
			element.setInitialState(initialState);
			return this;
		}

		public Builder onChange(Action onChange) {
			element.setOnChangeAction(onChange);
			return this;
		}

		public Builder onChange(Function<CheckBox, Action> onChange) {
			element.setOnChangeAction(onChange.apply(element));
			return this;
		}

		public Builder disabled(Supplier<Boolean> disabled) {
			element.setDisabled(disabled);
			return this;
		}

		public Builder disabled(boolean disabled) {
			element.setDisabled(disabled);
			return this;
		}

		@Override
		public CheckBox create() throws IllegalStateException {
			return super.create();
		}

	}

}
