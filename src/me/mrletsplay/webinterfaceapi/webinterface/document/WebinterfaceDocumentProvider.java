package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import me.mrletsplay.webinterfaceapi.http.document.DefaultDocumentProvider;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class WebinterfaceDocumentProvider extends DefaultDocumentProvider {
	
	@Override
	public HttpDocument getDocument(String path) {
		HttpDocument doc = super.getDocument(path);
		if(doc == null) {
			Webinterface.loadIncludedFiles(); // Try refreshing included files TODO: better method?
			return super.getDocument(path);
		}
		return doc;
	}

	@Override
	public void registerFileDocument(String path, File file, boolean appendFileName) {
		if(appendFileName
				&& file.isFile()
				&& Webinterface.getConfig()
				.getSetting(DefaultSettings.INDEX_FILES)
				.contains(file.getName().toLowerCase())) {
			super.registerFileDocument(path, file, false);
		}
		super.registerFileDocument(path, file, appendFileName);
	}
	
	@Override
	public HttpDocument createFileDocument(File file) {
		if(file.getName().endsWith(".php")) {
			return new CachingPHPFileDocument(file);
		}
		try {
			String mimeType = Files.probeContentType(file.toPath());
			return new CachingFileDocument(file, mimeType);
		} catch (IOException e) {
			return new CachingFileDocument(file);
		}
	}
	
}
