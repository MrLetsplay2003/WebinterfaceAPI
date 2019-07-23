package me.mrletsplay.webinterfaceapi.webinterface.page.element;

public enum WebinterfaceElementLayout {

	NONE(null),
	FULL_WIDTH("el-full-width"),
	FULL_WIDTH_LEFTBOUND("el-full-width el-leftbound"),
	FULL_WIDTH_RIGHTBOUND("el-full-width el-rightbound"),
	LEFTBOUND("el-leftbound"),
	RIGHTBOUND("el-rightbound"),
	;
	
	private final String className;
	
	private WebinterfaceElementLayout(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}
	
}
