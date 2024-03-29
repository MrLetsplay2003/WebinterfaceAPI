package me.mrletsplay.webinterfaceapi.document;

import me.mrletsplay.simplehttpserver.http.HttpRequestMethod;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class HomeDocument implements HttpDocument {

	@Override
	public void createContent() {
		String path = Webinterface.getConfig().getSetting(DefaultSettings.HOME_PAGE_PATH);
		HttpDocument d = Webinterface.getDocumentProvider().get(HttpRequestMethod.GET, path);
		if(d == null) {
			Webinterface.getDocumentProvider().getNotFoundDocument().createContent();
			return;
		}

		d.createContent();
	}

}
