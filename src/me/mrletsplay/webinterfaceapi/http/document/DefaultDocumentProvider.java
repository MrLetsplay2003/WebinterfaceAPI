package me.mrletsplay.webinterfaceapi.http.document;

import java.util.HashMap;
import java.util.Map;

public class DefaultDocumentProvider implements HttpDocumentProvider {

	private Map<String, HttpDocument> documents;
	private HttpDocument document404;
	
	public DefaultDocumentProvider() {
		this.documents = new HashMap<>();
		set404Document(new Default404Document());
	}
	
	@Override
	public void registerDocument(String path, HttpDocument document) {
		documents.put(path, document);
	}

	@Override
	public HttpDocument getDocument(String path) {
		return documents.get(path);
	}
	
	@Override
	public void set404Document(HttpDocument document) {
		this.document404 = document;
	}

	@Override
	public HttpDocument get404Document() {
		return document404;
	}

}
