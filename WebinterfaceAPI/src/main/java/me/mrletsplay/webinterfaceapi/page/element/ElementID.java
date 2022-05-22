package me.mrletsplay.webinterfaceapi.page.element;

import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class ElementID {

	private String id;

	public ElementID() {
		this.id = null;
	}

	public void set(String id) {
		this.id = id;
	}

	public String get() {
		return id;
	}

	public void require() {
		if(id == null) id = "e_" + WebinterfaceUtils.randomID(16);
	}

}
