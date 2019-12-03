package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;

public class WebinterfaceButton extends AbstractWebinterfacePageElement {
	
	private Supplier<String> text;
	private WebinterfaceAction onClickAction;
	
	public WebinterfaceButton(Supplier<String> text) {
		this.text = text;
	}
	
	public WebinterfaceButton(String text) {
		this(() -> text);
	}
	
	public void setText(Supplier<String> text) {
		this.text = text;
	}
	
	public void setText(String text) {
		setText(() -> text);
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlButton b = HtmlElement.button();
		b.setText(text);
		if(onClickAction != null) {
			JavaScriptScript sc = (JavaScriptScript) HttpRequestContext.getCurrentContext().getProperty(WebinterfacePage.CONTEXT_PROPERTY_SCRIPT);
			JavaScriptFunction f = onClickAction.toJavaScript();
			sc.addFunction(f);
			b.setOnClick(f.getSignature());
		}
		return b;
	}

}
