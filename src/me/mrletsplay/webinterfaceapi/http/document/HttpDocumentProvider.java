package me.mrletsplay.webinterfaceapi.http.document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public interface HttpDocumentProvider {
	
	public void registerDocument(String path, HttpDocument document);
	
	public default void registerFileDocument(String path, File file, boolean includeFolderName) {
		if(file.isDirectory()) {
			for(File fl : file.listFiles()) {
				registerFileDocument(includeFolderName ? (path + "/" + file.getName()) : path, fl, true);
			}
			return;
		}
		if(file.getName().endsWith(".php")) { // TODO: php is weird, außerdem index files etc
			if(file.getName().equalsIgnoreCase("index.php")) {
				registerDocument(path + "/", new PHPFileDocument(file));
			}
			registerDocument(path + "/" + file.getName(), new PHPFileDocument(file));
			return;
		}
		try {
			String mimeType = Files.probeContentType(file.toPath());
			registerDocument(path + "/" + file.getName(), new FileDocument(file, mimeType));
		} catch (IOException e) {
			registerDocument(path + "/" + file.getName(), new FileDocument(file));
		}
	}
	
	public HttpDocument getDocument(String path);

	public void set404Document(HttpDocument document);

	public HttpDocument get404Document();
	
}
