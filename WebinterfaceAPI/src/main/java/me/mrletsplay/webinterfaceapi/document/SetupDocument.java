package me.mrletsplay.webinterfaceapi.document;

import me.mrletsplay.simplehttpserver.dom.html.HtmlDocument;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.setup.SetupElement;
import me.mrletsplay.webinterfaceapi.setup.SetupElementType;
import me.mrletsplay.webinterfaceapi.setup.SetupStep;

public class SetupDocument implements HttpDocument {

	@Override
	public void createContent() {
		SetupStep step = Webinterface.getSetup().getNextStep();
		if(step == null) {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			ctx.getServerHeader().getFields().set("Location", "/");

			Webinterface.getDocumentProvider().unregisterDocument("/setup");
			Webinterface.getDocumentProvider().unregisterDocument("/setup/submit");

			return;
		}

		HtmlDocument d = new HtmlDocument();
		d.setTitle("Setup");
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

		HtmlElement ul = new HtmlElement("div");
		ul.addClass("setup-list");
		cont.appendChild(ul);

		HtmlElement c2 = new HtmlElement("div");
		c2.setID("setup-form-container");
		ul.appendChild(c2);

		addTitle(c2, step.getName());
		if(step.getDescription() != null) addDescription(c2, step.getDescription());
		for(SetupElement e : step.getElements()) {
			switch(e.getType()) {
				case STRING:
					addInputField(c2, e.getID(), e.getName(), (String) e.getInitialValue(), e.getType());
					break;
				case PASSWORD:
					addInputField(c2, e.getID(), e.getName(), null, e.getType());
					break;
				case INTEGER:
					addInputField(c2, e.getID(), e.getName(), e.getInitialValue() == null ? null : String.valueOf(e.getInitialValue()), e.getType());
					break;
				case DOUBLE:
					addInputField(c2, e.getID(), e.getName(), e.getInitialValue() == null ? null : String.valueOf(e.getInitialValue()), e.getType());
					break;
				case BOOLEAN:
					addCheckbox(c2, e.getID(), e.getName(), e.getInitialValue() == null ? false : (boolean) e.getInitialValue());
					break;
				case CHOICE:
					break; // TODO: implement
				case HEADING:
					addSubTitle(c2, e.getName());
					break;
				default:
					throw new UnsupportedOperationException("Unsupported setup element type: " + e.getType());
			}
		}

		addButton(ul, "Confirm", "setupConfirm()");

//		switch(currentStep) {
//			case SETUP_STEP_AUTH:
//			{
//				addTitle(c2, "Configure authentication methods");
//				addDescription(c2, "Set up authentication methods for people to log in with");
//				addCheckbox(c2, "no-auth", "Allow anonymous login", true);
//				addSubTitle(c2, "Discord");
//				addCheckbox(c2, "discord-auth", "Enable Discord auth", false);
//				addInputField(c2, "discord-client-id", "Discord client ID", false);
//				addInputField(c2, "discord-client-secret", "Discord client secret", false);
//				addSubTitle(c2, "Google");
//				addCheckbox(c2, "google-auth", "Enable Google auth", false);
//				addInputField(c2, "google-client-id", "Google client ID", false);
//				addInputField(c2, "google-client-secret", "Google client secret", false);
//				addSubTitle(c2, "GitHub");
//				addCheckbox(c2, "github-auth", "Enable GitHub auth", false);
//				addInputField(c2, "github-client-id", "GitHub client ID", false);
//				addInputField(c2, "github-client-secret", "GitHub client secret", false);
//				addButton(ul, "Confirm", "configureAuth()");
//				break;
//			}
//			default:
//			{
//				addTitle(ul, "Setup is done");
//				addButton(ul, "Confirm", "setupDone()");
//				break;
//			}
//		}

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

	private void addInputField(HtmlElement element, String name, String title, String defaultValue, SetupElementType type) {
		HtmlElement a = new HtmlElement("span");
		a.addClass("setup-label");
		a.setText(title);
		element.appendChild(a);

		HtmlElement input = new HtmlElement("input");
		input.setSelfClosing(true);
		input.addClass("setup-element");
		input.setAttribute("data-name", name);
		input.setAttribute("data-type", type.name().toLowerCase());
		input.setAttribute("type", type == SetupElementType.PASSWORD ? "password" : "text");
		input.setAttribute("name", name);
		if(defaultValue != null) input.setAttribute("value", defaultValue);
		element.appendChild(input);
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
		ch.addClass("setup-element");
		ch.setAttribute("data-name", name);
		ch.setAttribute("data-type", "boolean");
		ch.setAttribute("type", "checkbox");
		ch.setAttribute("aria-label", name); // TODO aria-label
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
