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
	public void registerFileDocument(String parentPath, File file, boolean includeFileName) {
		if(includeFileName
				&& file.isFile()
				&& Webinterface.getConfiguration()
				.getStringListSetting(DefaultSettings.INDEX_FILES)
				.contains(file.getName().toLowerCase())) {
			super.registerFileDocument(parentPath, file, false);
		}
		super.registerFileDocument(parentPath, file, includeFileName);
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
