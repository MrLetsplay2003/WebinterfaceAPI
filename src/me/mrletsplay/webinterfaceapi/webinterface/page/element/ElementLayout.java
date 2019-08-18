package me.mrletsplay.webinterfaceapi.webinterface.page.element;

public enum ElementLayout {

	FULL_WIDTH("el-full-width"),
	LEFTBOUND("el-leftbound"),
	RIGHTBOUND("el-rightbound"),
	JUSTIFY_TEXT("el-justify-text"),
	CENTER_VERTICALLY("el-center-vertically"),
	FULL_NOT_LAST_COLUMN("el-full-not-last-column"),
	NEW_LINE("el-new-line"),
//	REMAINING_SPACE("el-remainingspace"),
	;
	
	private final String className;
	
	private ElementLayout(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}
	
}
