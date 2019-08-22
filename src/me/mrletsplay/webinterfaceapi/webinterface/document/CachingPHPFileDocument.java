package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.mrletsplay.webinterfaceapi.http.document.PHPFileDocument;
import me.mrletsplay.webinterfaceapi.http.header.HttpClientHeader;
import me.mrletsplay.webinterfaceapi.http.header.HttpServerHeader;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

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
		if(!Webinterface.getConfiguration().getBooleanSetting(DefaultSettings.ENABLE_FILE_CACHING)) {
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
