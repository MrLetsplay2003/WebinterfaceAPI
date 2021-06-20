package me.mrletsplay.webinterfaceapi.http.document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import me.mrletsplay.webinterfaceapi.php.PHP;
import me.mrletsplay.webinterfaceapi.php.PHPFileDocument;

public interface HttpDocumentProvider {
	
	public void registerDocument(String path, HttpDocument document);
	
	public void unregisterDocument(String path);
	
	public default void registerFileDocument(String path, File file) {
		if(file.isDirectory()) {
			for(File fl : file.listFiles()) {
				registerFileDocument(path + "/" + fl.getName(), fl);
			}
			return;
		}
		registerDocument(path, createFileDocument(file));
	}
	
	public default HttpDocument createFileDocument(File file) {
		if(PHP.getFileExtensions().stream().anyMatch(file.getName()::endsWith)) {
			return new PHPFileDocument(file);
		}
		try {
			String mimeType = Files.probeContentType(file.toPath());
			return new FileDocument(file, mimeType);
		} catch (IOException e) {
			return new FileDocument(file);
		}
	}
	
	public HttpDocument getDocument(String path);

	public void set404Document(HttpDocument document);

	public HttpDocument get404Document();

	public void set500Document(HttpDocument document);

	public HttpDocument get500Document();
	
}
