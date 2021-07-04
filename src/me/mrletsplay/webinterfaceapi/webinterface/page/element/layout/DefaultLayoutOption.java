package me.mrletsplay.webinterfaceapi.webinterface.page.element.layout;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public enum DefaultLayoutOption implements ElementLayoutOption {

	FULL_WIDTH("el-full-width", true),
	LEFTBOUND("el-leftbound", true),
	RIGHTBOUND("el-rightbound", true),
	JUSTIFY_TEXT("el-justify-text", true),
	CENTER_VERTICALLY("el-center-vertically", true),
	CENTER_TEXT("el-center-text", true),
	FULL_NOT_LAST_COLUMN("el-full-not-last-column", true),
	SECOND_TO_LAST_COLUMN("el-second-to-last-column", true),
	SECOND_NOT_LAST_COLUMN("el-second-not-last-column", true),
	NEW_LINE("el-new-line", true),
	MAXIMIZE_COLUMNS("el-maximize-columns", false),
//	REMAINING_SPACE("el-remainingspace"),
	;
	
	private final String className;
	private final boolean isContainer;
	
	private DefaultLayoutOption(String className, boolean isContainer) {
		this.className = className;
		this.isContainer = isContainer;
	}
	
	@Override
	public void apply(HtmlElement elementContainer, HtmlElement element) throws UnsupportedOperationException {
		if(isContainer && elementContainer != null) {
			elementContainer.addClass(className);
		}else {
			element.addClass(className);
		}
	}
	
}
