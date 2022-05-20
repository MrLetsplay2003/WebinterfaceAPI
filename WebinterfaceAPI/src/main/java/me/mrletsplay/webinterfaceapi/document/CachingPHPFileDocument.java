package me.mrletsplay.webinterfaceapi.document;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.mrletsplay.simplehttpserver.http.header.HttpClientHeader;
import me.mrletsplay.simplehttpserver.http.header.HttpServerHeader;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.php.PHPFileDocument;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class CachingPHPFileDocument extends PHPFileDocument {

	private Map<HttpClientHeader, HttpServerHeader> cachedContent;

	public CachingPHPFileDocument(File file, String mimeType) {
		super(file, mimeType);
		this.cachedContent = new HashMap<>();
	}

	public CachingPHPFileDocument(File file) {
		super(file);
		this.cachedContent = new HashMap<>();
	}

	@Override
	public void createContent() {
		if(!Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_FILE_CACHING)) {
			super.createContent();
			return;
		}
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		HttpClientHeader ch = c.getClientHeader();
		HttpServerHeader sh = cachedContent.get(ch);
		if(sh == null) {
			super.createContent();
			cachedContent.put(ch, c.getServerHeader());
			return;
		}
		c.setServerHeader(sh);
	}

}
