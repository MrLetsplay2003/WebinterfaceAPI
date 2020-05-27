package me.mrletsplay.webinterfaceapi.webinterface.document;

import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;

public class WebinterfacePasswordLoginDocument implements HttpDocument {

	@Override
	public void createContent() {
		HtmlDocument d = new HtmlDocument();
		
		HtmlElement cont = new HtmlElement("div");
		cont.addClass("login-container");
		
		HtmlElement tt = new HtmlElement("div");
		tt.addClass("login-title");
		cont.appendChild(tt);
		
		HtmlElement ttx = new HtmlElement("a");
		ttx.setText("Log In");
		tt.appendChild(ttx);
		
		HtmlElement ul = new HtmlElement("ul");
		ul.addClass("login-list");
		cont.appendChild(ul);
		
		HtmlElement uDiv = new HtmlElement("div");
		uDiv.addClass("login-list-item");
		ul.appendChild(uDiv);
		
		HtmlElement uInput = new HtmlElement("input");
		uInput.setID("username-input");
		uInput.setAttribute("placeholder", "Username");
		uDiv.appendChild(uInput);
		
		HtmlElement pwDiv = new HtmlElement("div");
		pwDiv.addClass("login-list-item");
		ul.appendChild(pwDiv);
		
		HtmlElement pwInput = new HtmlElement("input");
		pwInput.setID("password-input");
		pwInput.setAttribute("placeholder", "Password");
		pwInput.setAttribute("type", "password");
		pwDiv.appendChild(pwInput);
		
		HtmlElement btnDiv = new HtmlElement("div");
		btnDiv.addClass("login-list-item login-button");
		ul.appendChild(btnDiv);
		
		HtmlElement lBtn = new HtmlElement("a");
		lBtn.setText("Log In");
		lBtn.setAttribute("onclick", "login()");
		btnDiv.appendChild(lBtn);
		
		d.getBodyNode().appendChild(cont);
		d.includeScript("https://code.jquery.com/jquery-3te.4.1.min.js", false, true);
		d.includeScript("/_internal/include.js", false, true);
		d.addStyleSheet("/_internal/password-login-include.css");
		d.includeScript("/_internal/password-login-include.js", true);
		d.createContent();
	}
	
}
