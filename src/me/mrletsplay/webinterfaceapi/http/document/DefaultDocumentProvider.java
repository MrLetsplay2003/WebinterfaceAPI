package me.mrletsplay.webinterfaceapi.http.document;

import java.util.HashMap;
import java.util.Map;

public class DefaultDocumentProvider implements HttpDocumentProvider {

	private Map<String, HttpDocument> documents;
	private HttpDocument
		document404,
		document500;
	
	public DefaultDocumentProvider() {
		this.documents = new HashMap<>();
		set404Document(new Default404Document());
		set500Document(new Default500Document());
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

	@Override
	public void set500Document(HttpDocument document) {
		this.document500 = document;
	}
	
	@Override
	public HttpDocument get500Document() {
		return document500;
	}

}
