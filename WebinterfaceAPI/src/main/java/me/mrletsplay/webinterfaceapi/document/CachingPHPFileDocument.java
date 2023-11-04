package me.mrletsplay.webinterfaceapi.document;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import me.mrletsplay.simplehttpserver.http.header.HttpClientHeader;
import me.mrletsplay.simplehttpserver.http.header.HttpServerHeader;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.util.MimeType;
import me.mrletsplay.simplehttpserver.php.PHPFileDocument;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

/**
 * @deprecated It's a bad idea to cache PHP responses like this
 */
@Deprecated
public class CachingPHPFileDocument extends PHPFileDocument {

	private Map<HttpClientHeader, HttpServerHeader> cachedContent;

	public CachingPHPFileDocument(Path path, MimeType mimeType) {
		super(path, mimeType);
		this.cachedContent = new HashMap<>();
	}

	public CachingPHPFileDocument(Path path) {
		super(path);
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
