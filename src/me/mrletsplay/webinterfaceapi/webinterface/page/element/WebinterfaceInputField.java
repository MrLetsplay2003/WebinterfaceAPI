package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;

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
		if(onChangeAction != null) {
			JavaScriptScript sc = (JavaScriptScript) HttpRequestContext.getCurrentContext().getProperty(WebinterfacePage.CONTEXT_PROPERTY_SCRIPT);
			JavaScriptFunction f = onChangeAction.toJavaScript();
			sc.addFunction(f);
			b.setAttribute("onchange", f.getSignature());
		}
		return b;
	}

}
