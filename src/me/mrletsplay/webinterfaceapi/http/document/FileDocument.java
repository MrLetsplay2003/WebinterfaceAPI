package me.mrletsplay.webinterfaceapi.http.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.server.ServerException;

public class FileDocument implements HttpDocument {
	
	private File file;
	private String mimeType;
	
	public FileDocument(File file, String mimeType) {
		this.file = file;
		this.mimeType = mimeType;
	}
	
	public FileDocument(File file) {
		this(file, null);
	}
	
	public File getFile() {
		return file;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	protected byte[] loadContent() {
		try(FileInputStream fI = new FileInputStream(file)) {
			return IOUtils.readAllBytes(fI);
		} catch (IOException e) {
			throw new ServerException("Failed to load file", e);
		}
	}

	@Override
	public void createContent() {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent(mimeType, loadContent());
	}

}
