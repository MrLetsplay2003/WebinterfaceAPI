package me.mrletsplay.webinterfaceapi.document;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.simplehttpserver.dom.html.HtmlDocument;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class LoginDocument implements HttpDocument {

	@Override
	public void createContent() {
		HtmlDocument d = new HtmlDocument();

		HtmlElement cont = new HtmlElement("div");
		cont.addClass("login-container");

		HtmlElement ttx = new HtmlElement("span");
		ttx.setText("Log In using");
		ttx.addClass("login-title");
		cont.appendChild(ttx);

		HtmlElement ul = new HtmlElement("ul");
		ul.addClass("login-list");
		cont.appendChild(ul);

		for(AuthMethod m : Webinterface.getAvailableAuthMethods()) {
			HtmlElement a = new HtmlElement("a");
			a.addClass("login-list-item");
			a.setText(m.getName());

			URLEncoded query = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery();

			boolean shouldConnect = query.has("connect") && query.getFirst("connect").equals("true");

			String href = "/auth/" + m.getID() + "?from=" + HttpUtils.urlEncode(query.getFirst("from", "/"))
				+ (shouldConnect ? "&connect=true" : "");

			a.setAttribute("href", () -> href);
			ul.appendChild(a);
		}

		if(Webinterface.getAvailableAuthMethods().isEmpty()) {
			HtmlElement a = new HtmlElement("a");
			a.setText("No login method is available at the moment");
			a.setAttribute("href", "#");
			ul.appendChild(a);
		}

		d.getBodyNode().appendChild(cont);
		d.addStyleSheet("/_internal/css/theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		d.addStyleSheet("/_internal/css/base.css");
		d.addStyleSheet("/_internal/css/login-include.css");
		d.createContent();
	}

}
