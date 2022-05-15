package me.mrletsplay.webinterfaceapi.webinterface.document;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.simplehttpserver.dom.html.HtmlDocument;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class LoginDocument implements HttpDocument {

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

		for(AuthMethod m : Webinterface.getAvailableAuthMethods()) {
			HtmlElement lo = new HtmlElement("div");
			lo.addClass("login-list-item");
			ul.appendChild(lo);

			HtmlElement a = new HtmlElement("a");
			a.setText(m.getName());

			URLEncoded query = HttpRequestContext.getCurrentContext().getRequestedPath().getQuery();

			boolean shouldConnect = query.has("connect") && query.getFirst("connect").equals("true");

			String href = "/auth/" + m.getID() + "?from=" + HttpUtils.urlEncode(query.getFirst("from", "/"))
				+ (shouldConnect ? "&connect=true" : "");

			a.setAttribute("href", () -> href);
			lo.appendChild(a);
		}

		if(Webinterface.getAvailableAuthMethods().isEmpty()) {
			HtmlElement lo = new HtmlElement("div");
			lo.addClass("login-list-item");
			ul.appendChild(lo);

			HtmlElement a = new HtmlElement("a");
			a.setText("No login method is available at the moment");
			a.setAttribute("href", "#");
			lo.appendChild(a);
		}

		d.getBodyNode().appendChild(cont);
		d.addStyleSheet("/_internal/css/theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		d.addStyleSheet("/_internal/css/login-include.css");
		d.createContent();
	}

}
