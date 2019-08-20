package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;

public class WebinterfaceCheckBox extends AbstractWebinterfacePageElement {
	
	private WebinterfaceAction onChangeAction;
	
	public void setOnChangeAction(WebinterfaceAction onChangeAction) {
		this.onChangeAction = onChangeAction;
	}
	
	@Override
	public HtmlElement createElement() {
//		HtmlElement w = new HtmlElement("div");
//		w.addClass("checkbox-wrapper");
//		w.setID(getOrGenerateID() + "-w");
//		
//		HtmlElement b = new HtmlElement("input");
//		b.setID(getOrGenerateID());
//		b.setAttribute("type", "checkbox");
//		b.setAttribute("aria-label", "Yes/No"); // TODO aria-label
//		if(onChangeAction != null) {
//			JavaScriptScript sc = (JavaScriptScript) HttpRequestContext.getCurrentContext().getProperty(WebinterfacePage.CONTEXT_PROPERTY_SCRIPT);
//			JavaScriptFunction f = onChangeAction.toJavaScript();
//			sc.addFunction(f);
//			b.setAttribute("onchange", f.getSignature());
//		}
//		w.appendChild(b);
//		
//		HtmlElement lbl = new HtmlElement("label");
//		lbl.addClass("checkbox-label");
//		lbl.setAttribute("for", getOrGenerateID());
//		w.appendChild(lbl);
//		return w;
		
		HtmlElement b = new HtmlElement("input");
		b.setAttribute("type", "checkbox");
		b.setAttribute("aria-label", "Yes/No"); // TODO aria-label
		if(onChangeAction != null) {
			JavaScriptScript sc = (JavaScriptScript) HttpRequestContext.getCurrentContext().getProperty(WebinterfacePage.CONTEXT_PROPERTY_SCRIPT);
			JavaScriptFunction f = onChangeAction.toJavaScript();
			sc.addFunction(f);
			b.setAttribute("onchange", f.getSignature());
		}
		return b;
	}

}
