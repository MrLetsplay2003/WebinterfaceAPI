package me.mrletsplay.webinterfaceapi.http.document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public interface HttpDocumentProvider {
	
	public void registerDocument(String path, HttpDocument document);
	
	public default void registerFileDocument(String parentPath, File file, boolean includeFileName) {
		if(file.isDirectory()) {
			for(File fl : file.listFiles()) {
				registerFileDocument(includeFileName ? (parentPath + "/" + file.getName()) : parentPath, fl, true);
			}
			return;
		}
		registerDocument(parentPath + "/" + (includeFileName ? file.getName() : ""), createFileDocument(file));
	}
	
	public default HttpDocument createFileDocument(File file) {
		if(file.getName().endsWith(".php")) {
			return new PHPFileDocument(file);
		}
		try {
			String mimeType = Files.probeContentType(file.toPath());
			return new FileDocument(file, mimeType);
		} catch (IOException e) {
			return new FileDocument(file);
		}
	}
	
	public default void registerFileDocument(String path, File file) {
		registerFileDocument(path, file, false);
	}
	
	public HttpDocument getDocument(String path);

	public void set404Document(HttpDocument document);

	public HttpDocument get404Document();
	
}
