package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceInputField extends AbstractWebinterfacePageElement {
	
	private Supplier<String>
		placeholder,
		initialValue;
	
	private WebinterfaceAction onChangeAction;
	
	public WebinterfaceInputField(Supplier<String> placeholder, Supplier<String> initialValue) {
		this.placeholder = placeholder;
		this.initialValue = initialValue;
	}
	
	public WebinterfaceInputField(String placeholder, String initialValue) {
		this(() -> placeholder, () -> initialValue);
	}
	
	public WebinterfaceInputField(Supplier<String> placeholder) {
		this(placeholder, null);
	}
	
	public WebinterfaceInputField(String placeholder) {
		this(() -> placeholder);
	}
	
	public WebinterfaceInputField() {
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
	
	public void setOnChangeAction(WebinterfaceAction onChangeAction) {
		this.onChangeAction = onChangeAction;
	}
	
	public ElementValue getValue() {
		return new ElementValue(this);
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("input");
		b.setAttribute("type", "text");
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
		return new Builder(new WebinterfaceInputField());
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceInputField, Builder> {

		private Builder(WebinterfaceInputField element) {
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
		
		public Builder onChange(WebinterfaceAction onChange) {
			element.setOnChangeAction(onChange);
			return this;
		}
		
		public Builder onChange(Function<WebinterfaceInputField, WebinterfaceAction> onChange) {
			element.setOnChangeAction(onChange.apply(element));
			return this;
		}
		
		@Override
		public WebinterfaceInputField create() throws IllegalStateException {
			return super.create();
		}
		
	}

}
