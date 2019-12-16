package me.mrletsplay.webinterfaceapi.webinterface.page.element.layout;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public enum DefaultLayoutProperty implements ElementLayoutProperty {

	FULL_WIDTH("el-full-width"),
	LEFTBOUND("el-leftbound"),
	RIGHTBOUND("el-rightbound"),
	JUSTIFY_TEXT("el-justify-text"),
	CENTER_VERTICALLY("el-center-vertically"),
	FULL_NOT_LAST_COLUMN("el-full-not-last-column"),
	SECOND_TO_LAST_COLUMN("el-second-to-last-column"),
	SECOND_NOT_LAST_COLUMN("el-second-not-last-column"),
	NEW_LINE("el-new-line"),
//	REMAINING_SPACE("el-remainingspace"),
	;
	
	private final String className;
	
	private DefaultLayoutProperty(String className) {
		this.className = className;
	}
	
	@Override
	public void apply(HtmlElement element) {
		element.addClass(className);
	}
	
}
