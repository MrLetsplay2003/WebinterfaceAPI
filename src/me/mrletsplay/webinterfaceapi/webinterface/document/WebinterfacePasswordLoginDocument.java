package me.mrletsplay.webinterfaceapi.webinterface.document;

import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

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
		
		HtmlElement ul = new HtmlElement("form");
		ul.setID("login-form");
		ul.addClass("login-list");
		ul.setAttribute("action", "/auth/password/response");
		ul.setAttribute("method", "post");
		cont.appendChild(ul);
		
		HtmlElement uDiv = new HtmlElement("div");
		uDiv.addClass("login-list-item");
		ul.appendChild(uDiv);
		
		HtmlElement uInput = new HtmlElement("input");
		uInput.setID("username-input");
		uInput.setAttribute("placeholder", "Username");
		uInput.setAttribute("name", "username");
		uDiv.appendChild(uInput);
		
		HtmlElement pwDiv = new HtmlElement("div");
		pwDiv.addClass("login-list-item");
		ul.appendChild(pwDiv);
		
		HtmlElement pwInput = new HtmlElement("input");
		pwInput.setID("password-input");
		pwInput.setAttribute("placeholder", "Password");
		pwInput.setAttribute("type", "password");
		pwInput.setAttribute("name", "password");
		pwDiv.appendChild(pwInput);
		
		HtmlElement regInput = new HtmlElement("input");
		regInput.setID("register-input");
		regInput.setAttribute("type", "checkbox");
		regInput.setAttribute("name", "register");
		regInput.setAttribute("style", "display:none;");
		ul.appendChild(regInput);
		
		HtmlElement btnDiv = new HtmlElement("div");
		btnDiv.addClass("login-list-item login-button");
		ul.appendChild(btnDiv);
		
		HtmlElement lBtn = new HtmlElement("a");
		lBtn.setText("Log In");
		lBtn.setAttribute("onclick", "login(false)");
		btnDiv.appendChild(lBtn);
		
		HtmlElement rBtnDiv = new HtmlElement("div");
		rBtnDiv.addClass("login-list-item register-button");
		ul.appendChild(rBtnDiv);
		
		HtmlElement rBtn = new HtmlElement("a");
		rBtn.setText("Register");
		rBtn.setAttribute("onclick", "login(true)");
		rBtnDiv.appendChild(rBtn);
		
		HtmlElement alertBox = new HtmlElement("div");
		alertBox.setID("alert-box");
		d.getBodyNode().appendChild(alertBox);
		
		d.getBodyNode().appendChild(cont);
		d.includeScript("https://code.jquery.com/jquery-3.5.1.min.js", false, true);
		d.includeScript("/_internal/js/module/toast.js", true, true);
		d.includeScript("/_internal/password-login-include.js", false, true);
		d.addStyleSheet("/_internal/theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		d.addStyleSheet("/_internal/password-login-include.css");
		d.addStyleSheet("/_internal/alerts.css");
		d.createContent();
	}
	
}
