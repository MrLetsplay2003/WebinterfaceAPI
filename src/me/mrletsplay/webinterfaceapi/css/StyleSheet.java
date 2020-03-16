package me.mrletsplay.webinterfaceapi.css;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;

public class StyleSheet implements HttpDocument {
	
	private List<CssElement>
		elements,
		mobileElements;
	
	public StyleSheet() {
		this.elements = new ArrayList<>();
		this.mobileElements = new ArrayList<>();
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
	
	public void addMobileElement(CssElement element) {
		mobileElements.add(element);
	}
	
	public void removeMobileElement(CssElement element) {
		mobileElements.remove(element);
	}
	
	public List<CssElement> getMobileElements() {
		return mobileElements;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for(CssElement el : elements) {
			b.append(el.toString());
		}
		
		if(!mobileElements.isEmpty()) {
			b.append("@media only screen and (max-width: 46.875em) {");
			for(CssElement el : mobileElements) {
				b.append(el.toString());
			}
			b.append("}");
		}
		return b.toString();
	}

	@Override
	public void createContent() {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent("text/css", toString().getBytes(StandardCharsets.UTF_8));
	}

}
