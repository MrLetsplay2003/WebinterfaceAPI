package me.mrletsplay.webinterfaceapi.webinterface.document;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.header.HttpURLPath;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

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
		
		for(WebinterfaceAuthMethod m : Webinterface.getAvailableAuthMethods()) {
			HtmlElement lo = new HtmlElement("div");
			lo.addClass("login-list-item");
			ul.appendChild(lo);
			
			HtmlElement a = new HtmlElement("a");
			a.setText(m.getName());
			
			HttpURLPath clientPath = HttpRequestContext.getCurrentContext().getClientHeader().getPath();
			
			boolean shouldConnect = clientPath.hasQueryParameter("connect") && clientPath.getQueryParameterValue("connect").equals("true");
			
			String href = "/auth/" + m.getID() + "?from=" + HttpUtils.urlEncode(clientPath.getQueryParameterValue("from", "/"))
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
		d.addStyleSheet("/_internal/theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		d.addStyleSheet("/_internal/login-include.css");
		d.createContent();
	}
	
}
