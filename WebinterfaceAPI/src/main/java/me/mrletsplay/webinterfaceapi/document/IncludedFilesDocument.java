package me.mrletsplay.webinterfaceapi.document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import me.mrletsplay.mrcore.http.HttpException;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.util.MimeType;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;

public class IncludedFilesDocument implements HttpDocument {

	private Map<Path, CachedFile> cached;

	public IncludedFilesDocument() {
		this.cached = new HashMap<>();
	}

	private CachedFile loadContentNoCache(Path path) {
		try {
			return new CachedFile(MimeType.of(Files.probeContentType(path)), Files.readAllBytes(path));
		} catch (IOException e) {
			throw new HttpException("Failed to load file", e);
		}
	}

	private CachedFile loadContent(Path path) {
		if(!Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_FILE_CACHING))
			return loadContentNoCache(path);
		CachedFile c = cached.get(path);
		if(c != null) return c;
		c = loadContentNoCache(path);
		cached.put(path, c);
		return c;
	}

	@Override
	public void createContent() {
		String path = HttpRequestContext.getCurrentContext().getPathParameters().get("path");
		Path p = Webinterface.getIncludePath().resolve(path).normalize();
		if(p.startsWith(Webinterface.getIncludePath()) && Files.isRegularFile(p)) { // Check that file exists and is actually in the directory
			CachedFile f = loadContent(p);
			HttpRequestContext.getCurrentContext().getServerHeader().setContent(f.mimeType, f.data);
		}else{
			Webinterface.getDocumentProvider().getNotFoundDocument().createContent();
		}
	}

	private static class CachedFile {
		public MimeType mimeType;
		public byte[] data;

		public CachedFile(MimeType mimeType, byte[] data) {
			this.mimeType = mimeType;
			this.data = data;
		}
	}

}
