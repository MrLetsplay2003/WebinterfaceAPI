package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.CheckboxValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceCheckBox extends AbstractWebinterfacePageElement {
	
	private WebinterfaceAction onChangeAction;
	private Supplier<Boolean> initialState;
	
	public WebinterfaceCheckBox(Supplier<Boolean> initialState) {
		this.initialState = initialState;
	}
	
	public WebinterfaceCheckBox(boolean initialState) {
		this(() -> initialState);
	}

	public WebinterfaceCheckBox() {
		this(() -> false);
	}
	
	public void setInitialState(Supplier<Boolean> initialState) {
		this.initialState = initialState;
	}
	
	public void setInitialState(boolean initialState) {
		setInitialState(() -> initialState);
	}
	
	public void setOnChangeAction(WebinterfaceAction onChangeAction) {
		this.onChangeAction = onChangeAction;
	}
	
	public Supplier<Boolean> getInitialState() {
		return initialState;
	}
	
	@Deprecated
	public CheckboxValue getCheckedValue() {
		return new CheckboxValue(this);
	}
	
	public CheckboxValue checkedValue() {
		return new CheckboxValue(this);
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement label = new HtmlElement("label");
		label.addClass("checkbox-container");
		HtmlElement ch = new HtmlElement("input");
		ch.setAttribute("type", "checkbox");
		ch.setAttribute("aria-label", "Yes/No"); // TODO aria-label
		if(initialState.get()) ch.setAttribute("checked");
		if(onChangeAction != null) ch.setAttribute("onchange", onChangeAction.createAttributeValue());
		label.appendChild(ch);
		HtmlElement sp = new HtmlElement("span");
		sp.addClass("checkbox-checkmark");
		label.appendChild(sp);
		return label;
	}
	
	public static Builder builder() {
		return new Builder(new WebinterfaceCheckBox());
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceCheckBox, Builder> {

		private Builder(WebinterfaceCheckBox element) {
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
		
		public Builder onChange(WebinterfaceAction onChange) {
			element.setOnChangeAction(onChange);
			return this;
		}
		
		public Builder onChange(Function<WebinterfaceCheckBox, WebinterfaceAction> onChange) {
			element.setOnChangeAction(onChange.apply(element));
			return this;
		}
		
		@Override
		public WebinterfaceCheckBox create() throws IllegalStateException {
			return super.create();
		}
		
	}

}
