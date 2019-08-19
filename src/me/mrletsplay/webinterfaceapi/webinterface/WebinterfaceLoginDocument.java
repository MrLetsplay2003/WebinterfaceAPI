package me.mrletsplay.webinterfaceapi.webinterface;

import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class WebinterfaceLoginDocument implements HttpDocument {

	@Override
	public void createContent() {
		HtmlDocument d = new HtmlDocument();
		for(WebinterfaceAuthMethod m : Webinterface.getAuthMethods()) {
			HtmlElement a = new HtmlElement("a");
			a.setText(m.getName());
			a.setAttribute("href", "/auth/" + m.getID());
			d.getBodyNode().appendChild(a);
			d.getBodyNode().appendChild(HtmlElement.br());
		}
		d.createContent();
	}
	
}
