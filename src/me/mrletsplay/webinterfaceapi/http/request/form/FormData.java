package me.mrletsplay.webinterfaceapi.http.request.form;

import java.util.List;

public class FormData {
	
	private List<FormElement> elements;

	public FormData(List<FormElement> elements) {
		this.elements = elements;
	}

	public List<FormElement> getElements() {
		return elements;
	}
	
	public FormElement getElement(String name) {
		return elements.stream()
				.filter(el -> name.equals(el.getProperties().get("name")))
				.findFirst().orElse(null);
	}

}
