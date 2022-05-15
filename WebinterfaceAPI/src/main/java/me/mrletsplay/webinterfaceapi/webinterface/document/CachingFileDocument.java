package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.io.File;

import me.mrletsplay.simplehttpserver.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class CachingFileDocument extends FileDocument {

	private long prevLastModified;
	private byte[] content;

	public CachingFileDocument(File file, String mimeType) {
		super(file, mimeType);
	}

	public CachingFileDocument(File file) {
		super(file);
	}

	@Override
	protected byte[] loadContent() {
		if(!Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_FILE_CACHING))
			return super.loadContent();
		if(content != null && prevLastModified == getFile().lastModified()) return content;
		prevLastModified = getFile().lastModified();
		return content = super.loadContent();
	}

}
