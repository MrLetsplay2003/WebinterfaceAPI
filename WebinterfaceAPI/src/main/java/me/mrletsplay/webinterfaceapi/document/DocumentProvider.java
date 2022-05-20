package me.mrletsplay.webinterfaceapi.document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import me.mrletsplay.simplehttpserver.http.document.DefaultDocumentProvider;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class DocumentProvider extends DefaultDocumentProvider {

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
	public void registerFileDocument(String path, File file) {
		if(file.isFile()
				&& Webinterface.getConfig()
				.getSetting(DefaultSettings.INDEX_FILES)
				.contains(file.getName().toLowerCase())) {
			super.registerFileDocument(Paths.get(path).getParent().toString(), file);
		}
		super.registerFileDocument(path, file);
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
