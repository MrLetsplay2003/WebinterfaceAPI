package me.mrletsplay.webinterfaceapi.webinterface.page.element;

public enum WebinterfaceElementLayout {

	NONE(""),
	FULL_WIDTH_BOX("box full-width-box"),
	HALF_WIDTH_BOX("box half-width-box"),
	SMALL_SQUARE_BOX("box small-square-box"),
	;
	
	private final String className;
	
	private WebinterfaceElementLayout(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}
	
}
