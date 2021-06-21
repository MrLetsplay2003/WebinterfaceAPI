package me.mrletsplay.webinterfaceapi.webinterface.document;

import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class WebinterfaceHomeDocument implements HttpDocument {

	@Override
	public void createContent() {
		Integer setupStep = Webinterface.getConfig().getOverride(WebinterfaceSetupDocument.SETUP_STEP_OVERRIDE_PATH, Integer.class);
		if(Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_INITIAL_SETUP) && (setupStep == null || setupStep < WebinterfaceSetupDocument.SETUP_STEP_DONE)) {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			ctx.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
			ctx.getServerHeader().getFields().setFieldValue("Location", "/setup");
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
