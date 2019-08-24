package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;

public class WebinterfaceSelect extends AbstractWebinterfacePageElement {
	
	private Supplier<Map<String, String>> options;
	private WebinterfaceAction onChangeAction;
	
	public WebinterfaceSelect() {
		this.options = LinkedHashMap::new;
	}
	
	public void addOption(String name, String value) {
		Supplier<Map<String, String>> o = this.options;
		this.options = () -> {
			Map<String, String> ops = o.get();
			ops.put(name, value);
			return ops;
		};
	}
	
	public void setOnChangeAction(WebinterfaceAction onChangeAction) {
		this.onChangeAction = onChangeAction;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("select");
//		b.setAttribute("placeholder", placeholder);
//		b.setAttribute("aria-label", placeholder);
		for(Map.Entry<String, String> op : options.get().entrySet()) {
			HtmlElement oe = new HtmlElement("option");
			oe.setText(op.getKey());
			oe.setAttribute("value", op.getValue());
			b.appendChild(oe);
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
