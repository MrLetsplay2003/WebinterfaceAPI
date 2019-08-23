package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;

public class WebinterfaceSelect extends AbstractWebinterfacePageElement {
	
	private Supplier<String> placeholder;
	private WebinterfaceAction onChangeAction;
	
	public WebinterfaceSelect() {
		this(() -> "Text");
	}
	
	public WebinterfaceSelect(Supplier<String> placeholder) {
		this.placeholder = placeholder;
	}
	
	public WebinterfaceSelect(String placeholder) {
		this(() -> placeholder);
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
	
	public void setOnChangeAction(WebinterfaceAction onChangeAction) {
		this.onChangeAction = onChangeAction;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("input");
		b.setAttribute("type", "text");
		b.setAttribute("placeholder", placeholder);
		b.setAttribute("aria-label", placeholder);
		if(onChangeAction != null) {
			JavaScriptScript sc = (JavaScriptScript) HttpRequestContext.getCurrentContext().getProperty(WebinterfacePage.CONTEXT_PROPERTY_SCRIPT);
			JavaScriptFunction f = onChangeAction.toJavaScript();
			sc.addFunction(f);
			b.setAttribute("onchange", f.getSignature());
		}
		return b;
	}

}
