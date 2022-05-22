package me.mrletsplay.webinterfaceapi.document;

import me.mrletsplay.simplehttpserver.dom.html.HtmlDocument;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class SetupDocument implements HttpDocument {

	public static final String
		SETUP_STEP_OVERRIDE_PATH = "initial-setup.current-step";

	public static final int
		SETUP_STEP_BASE = 0,
		SETUP_STEP_HTTP = 1,
		SETUP_STEP_AUTH = 2,
		SETUP_STEP_DONE = 3;

	@Override
	public void createContent() {
		Integer currentStep = Webinterface.getConfig().getOverride(SETUP_STEP_OVERRIDE_PATH, Integer.class);
		if(currentStep == null) currentStep = SETUP_STEP_BASE;

		HtmlDocument d = new HtmlDocument();
		d.setTitle("Login");
		d.setIcon("/_internal/include/img/" + Webinterface.getConfig().getSetting(DefaultSettings.ICON_IMAGE));
		d.setLanguage("en");

		HtmlElement cont = new HtmlElement("div");
		cont.addClass("setup-container");

		HtmlElement tt = new HtmlElement("div");
		tt.addClass("setup-title");
		cont.appendChild(tt);

		HtmlElement ttx = new HtmlElement("span");
		ttx.setText("WebinterfaceAPI Setup");
		tt.appendChild(ttx);

		HtmlElement ul = new HtmlElement("form");
		ul.setID("setup-form");
		ul.setAttribute("method", "post");
		ul.setAttribute("action", "/setup/submit");
		ul.addClass("setup-list");
		cont.appendChild(ul);

		HtmlElement c2 = new HtmlElement("div");
		c2.setID("setup-form-container");
		ul.appendChild(c2);

		switch(currentStep) {
			case SETUP_STEP_BASE:
			{
				addTitle(c2, "Create an Administrator account");
				addDescription(c2, "This account will be used to make further changes to the configuration");
				addInputField(c2, "admin-name", "Username", false);
				addInputField(c2, "admin-password", "Password", true);
				addInputField(c2, "admin-password-repeat", "Repeat Password", true);
				addButton(ul, "Confirm", "createAdminAccount()");
				break;
			}
			case SETUP_STEP_HTTP:
			{
				addTitle(c2, "Set up the web server");
				addDescription(c2, "Configure your server so it works as smoothly as possible");
				addSubTitle(c2, "HTTP");
				addInputField(c2, "http-bind", "HTTP IP Bind", DefaultSettings.HTTP_BIND.getDefaultValue(), false);
				addInputField(c2, "http-host", "HTTP Host", DefaultSettings.HTTP_HOST.getDefaultValue(), false);
				addInputField(c2, "http-port", "HTTP Port", String.valueOf(DefaultSettings.HTTP_PORT.getDefaultValue()), false);
				addSubTitle(c2, "HTTPS");
				addCheckbox(c2, "enable-https", "Enable HTTPS", false);
				addInputField(c2, "https-bind", "HTTPS IP Bind", DefaultSettings.HTTPS_BIND.getDefaultValue(), false);
				addInputField(c2, "https-host", "HTTPS Host", DefaultSettings.HTTPS_HOST.getDefaultValue(), false);
				addInputField(c2, "https-port", "HTTPS Port", String.valueOf(DefaultSettings.HTTPS_PORT.getDefaultValue()), false);
				addInputField(c2, "https-cert-path", "HTTPS Certificate Path", false);
				addInputField(c2, "https-cert-password", "HTTPS Certificate Password (leave empty for none)", false);
				addInputField(c2, "https-cert-key-path", "HTTPS Certificate Key Path", false);
				addButton(ul, "Confirm", "configureHTTP()");
				break;
			}
			case SETUP_STEP_AUTH:
			{
				addTitle(c2, "Configure authentication methods");
				addDescription(c2, "Set up authentication methods for people to log in with");
				addCheckbox(c2, "no-auth", "Allow anonymous login", true);
				addSubTitle(c2, "Discord");
				addCheckbox(c2, "discord-auth", "Enable Discord auth", false);
				addInputField(c2, "discord-client-id", "Discord client ID", false);
				addInputField(c2, "discord-client-secret", "Discord client secret", false);
				addSubTitle(c2, "Google");
				addCheckbox(c2, "google-auth", "Enable Google auth", false);
				addInputField(c2, "google-client-id", "Google client ID", false);
				addInputField(c2, "google-client-secret", "Google client secret", false);
				addSubTitle(c2, "GitHub");
				addCheckbox(c2, "github-auth", "Enable GitHub auth", false);
				addInputField(c2, "github-client-id", "GitHub client ID", false);
				addInputField(c2, "github-client-secret", "GitHub client secret", false);
				addButton(ul, "Confirm", "configureAuth()");
				break;
			}
			default:
			{
				addTitle(ul, "Setup is done");
				addButton(ul, "Confirm", "setupDone()");
				break;
			}
		}

		HtmlElement alertBox = new HtmlElement("div");
		alertBox.setID("alert-box");
		d.getBodyNode().appendChild(alertBox);

		d.getBodyNode().appendChild(cont);
		d.addStyleSheet("/_internal/include/css/theme/" + Webinterface.getConfig().getSetting(DefaultSettings.THEME) + ".css");
		d.addStyleSheet("/_internal/include/css/base.css");
		d.addStyleSheet("/_internal/include/css/setup-include.css");
		d.addStyleSheet("/_internal/include/css/alerts.css");
		d.includeScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js", false, true);
		d.includeScript("/_internal/include/js/module/toast.js", true, true);
		d.includeScript("/_internal/include/js/setup-include.js", false, true);
		d.createContent();
	}

	private void addInputField(HtmlElement element, String name, String title, String defaultValue, boolean password) {
		HtmlElement a = new HtmlElement("span");
		a.addClass("setup-label");
		a.setText(title);
		element.appendChild(a);

		HtmlElement input = new HtmlElement("input");
		input.setSelfClosing(true);
		input.setID(name);
		input.setAttribute("type", password ? "password" : "text");
		input.setAttribute("name", name);
		if(defaultValue != null) input.setAttribute("value", defaultValue);
		element.appendChild(input);
	}

	private void addInputField(HtmlElement element, String name, String title, boolean password) {
		addInputField(element, name, title, null, password);
	}

	private void addTitle(HtmlElement element, String title) {
		HtmlElement b = new HtmlElement("b");
		b.addClass("setup-heading");
		b.setText(title);
		element.appendChild(b);
	}

	private void addSubTitle(HtmlElement element, String title) {
		HtmlElement b = new HtmlElement("b");
		b.addClass("setup-sub-heading");
		b.setText(title);
		element.appendChild(b);
	}

	private void addDescription(HtmlElement element, String description) {
		HtmlElement b = new HtmlElement("span");
		b.addClass("setup-description");
		b.setText(description);
		element.appendChild(b);
	}

	private void addButton(HtmlElement element, String name, String onclick) {
		HtmlElement b = new HtmlElement("button");
		b.addClass("setup-button");
		b.setText(name);
		b.setAttribute("onclick", onclick);
		element.appendChild(b);
	}

	private void addCheckbox(HtmlElement element, String name, String title, boolean initialState) {
		HtmlElement container = new HtmlElement("div");
		container.addClass("setup-checkbox-container");

		HtmlElement label = new HtmlElement("label");
		label.addClass("checkbox-container");
		HtmlElement ch = new HtmlElement("input");
		ch.setSelfClosing(true);
		ch.setID(name);
		ch.setAttribute("name", name);
		ch.setAttribute("type", "checkbox");
		ch.setAttribute("aria-label", "Yes/No"); // TODO aria-label
		if(initialState) ch.setAttribute("checked");
		label.appendChild(ch);
		HtmlElement sp = new HtmlElement("span");
		sp.addClass("checkbox-checkmark");
		label.appendChild(sp);
		container.appendChild(label);

		HtmlElement a = new HtmlElement("span");
		a.addClass("setup-label");
		a.setText(title);
		container.appendChild(a);

		element.appendChild(container);
	}

}
