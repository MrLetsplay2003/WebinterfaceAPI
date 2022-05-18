package me.mrletsplay.webinterfaceapi.webinterface.document;

import me.mrletsplay.simplehttpserver.dom.html.HtmlDocument;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class PasswordLoginDocument implements HttpDocument {

	@Override
	public void createContent() {
		HtmlDocument d = new HtmlDocument();

		HtmlElement cont = new HtmlElement("div");
		cont.addClass("login-container");

		HtmlElement ttx = new HtmlElement("span");
		ttx.setText("Log In");
		ttx.addClass("login-title");
		cont.appendChild(ttx);

		HtmlElement ul = new HtmlElement("form");
		ul.setID("login-form");
		ul.addClass("login-list");
		ul.setAttribute("action", "/auth/password/response");
		ul.setAttribute("method", "post");
		cont.appendChild(ul);

		HtmlElement uInput = new HtmlElement("input");
		uInput.setSelfClosing(true);
		uInput.setID("username-input");
		uInput.addClass("login-list-item");
		uInput.setAttribute("type", "text");
		uInput.setAttribute("placeholder", "Username");
		uInput.setAttribute("name", "username");
		ul.appendChild(uInput);

		HtmlElement pwInput = new HtmlElement("input");
		pwInput.setSelfClosing(true);
		pwInput.setID("password-input");
		pwInput.addClass("login-list-item");
		pwInput.setAttribute("type", "text");
		pwInput.setAttribute("placeholder", "Password");
		pwInput.setAttribute("type", "password");
		pwInput.setAttribute("name", "password");
		ul.appendChild(pwInput);

		HtmlElement regInput = new HtmlElement("input");
		regInput.setSelfClosing(true);
		regInput.setID("register-input");
		regInput.setAttribute("type", "checkbox");
		regInput.setAttribute("name", "register");
		regInput.setAttribute("style", "display:none;");
		ul.appendChild(regInput);

		HtmlElement lBtn = new HtmlElement("button");
		lBtn.setText("Log In");
		lBtn.addClass("login-button");
		lBtn.setAttribute("onclick", "login(false)");
		ul.appendChild(lBtn);

		HtmlElement rBtn = new HtmlElement("button");
		rBtn.setText("Register");
		rBtn.addClass("login-button");
		rBtn.setAttribute("onclick", "login(true)");
		ul.appendChild(rBtn);

		HtmlElement alertBox = new HtmlElement("div");
		alertBox.setID("alert-box");
		d.getBodyNode().appendChild(alertBox);

		d.getBodyNode().appendChild(cont);
		d.includeScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js", false, true);
		d.includeScript("/_internal/js/module/toast.js", true, true);
		d.includeScript("/_internal/js/password-login-include.js", false, true);
		d.addStyleSheet("/_internal/css/theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		d.addStyleSheet("/_internal/css/base.css");
		d.addStyleSheet("/_internal/css/login-include.css");
		d.addStyleSheet("/_internal/css/alerts.css");
		d.createContent();
	}

}
