package me.mrletsplay.webinterfaceapi.webinterface;

import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;

public class WebinterfaceLoginDocument implements HttpDocument {

	@Override
	public void createContent() {
		HtmlDocument d = new HtmlDocument();
		
		HtmlElement cont = new HtmlElement("div");
		cont.addClass("login-container");
		
		HtmlElement tt = new HtmlElement("div");
		tt.addClass("login-title");
		cont.appendChild(tt);
		
		HtmlElement ttx = new HtmlElement("a");
		ttx.setText("Log In using");
		tt.appendChild(ttx);
		
		HtmlElement ul = new HtmlElement("ul");
		ul.addClass("login-list");
		cont.appendChild(ul);
		
		for(WebinterfaceAuthMethod m : Webinterface.getAuthMethods()) {
			HtmlElement lo = new HtmlElement("div");
			lo.addClass("login-list-item");
			ul.appendChild(lo);
			
			HtmlElement a = new HtmlElement("a");
			a.setText(m.getName());
			a.setAttribute("href", "/auth/" + m.getID());
			lo.appendChild(a);
		}
		
		d.getBodyNode().appendChild(cont);
//		d.includeScript("/_internal/login-include.js", true);
		d.addStyleSheet("/_internal/login-include.css");
		d.createContent();
	}
	
}
