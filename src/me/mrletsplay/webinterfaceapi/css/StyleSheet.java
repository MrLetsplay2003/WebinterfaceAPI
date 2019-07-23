package me.mrletsplay.webinterfaceapi.css;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;

public class StyleSheet implements HttpDocument {
	
	private List<CssElement> elements;
	
	public StyleSheet() {
		this.elements = new ArrayList<>();
	}
	
	public void addElement(CssElement element) {
		elements.add(element);
	}
	
	public void removeElement(CssElement element) {
		elements.remove(element);
	}
	
	public List<CssElement> getElements() {
		return elements;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for(CssElement el : elements) {
			b.append(el.toString());
		}
		return b.toString();
	}

	@Override
	public void createContent() {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent("text/css", toString().getBytes(StandardCharsets.UTF_8));
	}

}
