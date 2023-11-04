package me.mrletsplay.webinterfaceapi.document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.FileDocument;
import me.mrletsplay.simplehttpserver.http.exception.HttpResponseException;
import me.mrletsplay.simplehttpserver.http.util.MimeType;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class CachingFileDocument extends FileDocument {

	private long prevLastModified;
	private byte[] content;

	public CachingFileDocument(Path path, MimeType mimeType) {
		super(path, mimeType);
	}

	public CachingFileDocument(Path path) throws IOException {
		super(path);
	}

	@Override
	protected byte[] loadContent() {
		try {
			if(!Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_FILE_CACHING))
				return super.loadContent();

			long lastModified = Files.getLastModifiedTime(getPath()).toMillis();
			if(content != null && prevLastModified == lastModified) return content;

			prevLastModified = lastModified;
			return content = super.loadContent();
		}catch(IOException e) {
			throw new HttpResponseException(HttpStatusCodes.INTERNAL_SERVER_ERROR_500, "Failed to load file");
		}
	}

}
