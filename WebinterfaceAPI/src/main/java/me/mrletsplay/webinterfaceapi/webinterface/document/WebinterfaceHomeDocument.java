package me.mrletsplay.webinterfaceapi.webinterface.document;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class WebinterfaceHomeDocument implements HttpDocument {

	@Override
	public void createContent() {
		Integer setupStep = Webinterface.getConfig().getOverride(WebinterfaceSetupDocument.SETUP_STEP_OVERRIDE_PATH, Integer.class);
		if(Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_INITIAL_SETUP) && (setupStep == null || setupStep < WebinterfaceSetupDocument.SETUP_STEP_DONE)) {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			ctx.getServerHeader().getFields().set("Location", "/setup");
			return;
		}

		String path = Webinterface.getConfig().getSetting(DefaultSettings.HOME_PAGE_PATH);
		HttpDocument d = Webinterface.getDocumentProvider().getDocument(path);
		if(d == null) {
			Webinterface.getDocumentProvider().get404Document().createContent();
			return;
		}

		d.createContent();
	}

}
