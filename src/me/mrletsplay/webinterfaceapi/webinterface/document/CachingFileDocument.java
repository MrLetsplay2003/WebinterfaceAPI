package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.io.File;

import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class CachingFileDocument extends FileDocument {
	
	private long lastFileSize;
	private byte[] content;

	public CachingFileDocument(File file, String mimeType) {
		super(file, mimeType);
	}

	public CachingFileDocument(File file) {
		super(file);
	}
	
	@Override
	protected byte[] loadContent() {
		if(!Webinterface.getConfiguration().getBooleanSetting(DefaultSettings.ENABLE_FILE_CACHING))
			return super.loadContent();
		// TODO: use lastmodified instead
		if(content != null && getFile().length() == lastFileSize) return content;
		return content = super.loadContent();
	}
	
}
