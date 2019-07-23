package me.mrletsplay.webinterfaceapi.http.document;

public interface HttpDocumentProvider {
	
	public void registerDocument(String path, HttpDocument document);

	public HttpDocument getDocument(String path);

	public void set404Document(HttpDocument document);

	public HttpDocument get404Document();
	
}
