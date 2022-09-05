package me.mrletsplay.webinterfaceapi.document;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import me.mrletsplay.simplehttpserver.dom.html.HtmlDocument;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.header.DefaultClientContentTypes;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.session.Session;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class RegistrationSecretDocument implements HttpDocument {

	// TODO: make entries temporary
	public static final Map<String, AccountConnection> ACTIVE_REGISTRATIONS = new HashMap<>();

	@Override
	public void createContent() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();

		if(!"secret".equals(Webinterface.getConfig().getSetting(DefaultSettings.REGISTRATION_MODE))) {
			ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			ctx.getServerHeader().getFields().set("Location", "/");
			return;
		}

		if(ctx.getClientHeader().getMethod().equalsIgnoreCase("POST") && ctx.getClientHeader().getPostData() != null) {
			URLEncoded enc = ctx.getClientHeader().getPostData().getParsedAs(DefaultClientContentTypes.URLENCODED);
			String secret = enc.getFirst("secret");
			String id = enc.getFirst("registration-id");
			if(id == null || !ACTIVE_REGISTRATIONS.containsKey(id)) {
				ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
				ctx.getServerHeader().getFields().set("Location", "/");
				return;
			}

			AccountConnection con = ACTIVE_REGISTRATIONS.get(id);

			if(secret != null && secret.equals(Webinterface.getConfig().getSetting(DefaultSettings.REGISTRATION_SECRET))) {
				ACTIVE_REGISTRATIONS.remove(id);

				con.runPostRegistrationCallback();
				Session s = Session.startSession(con);
				ctx.getServerHeader().getFields().setCookie(Session.COOKIE_NAME, s.getSessionID(), "Path=/", "Expires=" + WebinterfaceUtils.httpTimeStamp(s.getExpiresAt()));

				ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
				ctx.getServerHeader().getFields().set("Location", "/");
				return;
			}else {
				ACTIVE_REGISTRATIONS.remove(id);
				ctx.getServerHeader().setStatusCode(HttpStatusCodes.ACCESS_DENIED_403);
				ctx.getServerHeader().setContent("text/plain", "Invalid secret".getBytes(StandardCharsets.UTF_8));
				return;
			}
		}

		String id = ctx.getClientHeader().getPath().getQuery().getFirst("id");
		if(id == null || !ACTIVE_REGISTRATIONS.containsKey(id)) {
			ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			ctx.getServerHeader().getFields().set("Location", "/");
			return;
		}

		HtmlDocument d = new HtmlDocument();
		d.setTitle("Login");
		d.setIcon("/_internal/include/img/" + Webinterface.getConfig().getSetting(DefaultSettings.ICON_IMAGE));
		d.setLanguage("en");

		HtmlElement cont = new HtmlElement("div");
		cont.addClass("login-container");

		HtmlElement ttx = new HtmlElement("span");
		ttx.setText("Input registration secret");
		ttx.addClass("login-title");
		cont.appendChild(ttx);

		HtmlElement ul = new HtmlElement("form");
		ul.setID("login-form");
		ul.addClass("login-list");
		ul.setAttribute("action", "/registration-secret");
		ul.setAttribute("method", "post");
		cont.appendChild(ul);

		HtmlElement pwInput = new HtmlElement("input");
		pwInput.setSelfClosing(true);
		pwInput.setID("secret-input");
		pwInput.addClass("login-list-item");
		pwInput.setAttribute("type", "text");
		pwInput.setAttribute("placeholder", "Registration secret");
		pwInput.setAttribute("type", "password");
		pwInput.setAttribute("name", "secret");
		ul.appendChild(pwInput);

		HtmlElement regInput = new HtmlElement("input");
		regInput.setSelfClosing(true);
		regInput.setID("register-input");
		regInput.setAttribute("type", "text");
		regInput.setAttribute("name", "registration-id");
		regInput.setAttribute("value", id);
		regInput.setAttribute("style", "display:none;");
		ul.appendChild(regInput);

		HtmlElement lBtn = new HtmlElement("button");
		lBtn.setText("Complete registration");
		lBtn.addClass("login-button");
		lBtn.setAttribute("onclick", "register(event)");
		ul.appendChild(lBtn);

		HtmlElement alertBox = new HtmlElement("div");
		alertBox.setID("alert-box");
		d.getBodyNode().appendChild(alertBox);

		d.getBodyNode().appendChild(cont);
		d.includeScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js", false, true);
		d.includeScript("/_internal/include/js/module/toast.js", false, true);
		d.includeScript("/_internal/include/js/registration-secret-include.js", false, true);
		d.addStyleSheet("/_internal/include/css/theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		d.addStyleSheet("/_internal/include/css/base.css");
		d.addStyleSheet("/_internal/include/css/login-include.css");
		d.addStyleSheet("/_internal/include/css/alerts.css");
		d.createContent();
	}

}
