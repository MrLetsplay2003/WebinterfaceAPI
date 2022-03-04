package me.mrletsplay.webinterfaceapi.webinterface.page.element.layout;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public enum DefaultLayoutOption implements ElementLayoutOption {

	FULL_WIDTH("el-full-width", true),
	JUSTIFY_TEXT("el-justify-text", true),
	LEFTBOUND_TEXT("el-leftbound-text", true),
	RIGHTBOUND_TEXT("el-leftbound-text", true),
	CENTER_TEXT("el-center-text", true),
	FULL_NOT_LAST_COLUMN("el-full-not-last-column", true),
	SECOND_TO_LAST_COLUMN("el-second-to-last-column", true),
	SECOND_NOT_LAST_COLUMN("el-second-not-last-column", true),
	NEW_LINE("el-new-line", true),
	MAXIMIZE_COLUMNS("el-maximize-columns", false),
	NO_PADDING("el-no-padding", true),
	ALIGN_LEFT("el-align-left", true),
	ALIGN_RIGHT("el-align-right", true),
	ALIGN_CENTER_HORIZONTALLY("el-align-center-horizontally", true),
	ALIGN_TOP("el-align-top", true),
	ALIGN_BOTTOM("el-align-bottom", true),
	ALIGN_CENTER_VERTICALLY("el-align-center-vertically", true),
	FIT_CONTENT("el-fit-content", false),
	PACK_CONTENT("el-pack-content", false),
	
	/**
	 * Use {@link #ALIGN_LEFT} instead
	 */
	@Deprecated
	LEFTBOUND("el-align-left", true),
	
	/**
	 * Use {@link #ALIGN_RIGHT} instead
	 */
	@Deprecated
	RIGHTBOUND("el-align-right", true),
	
	/**
	 * Use {@link #ALIGN_CENTER_HORIZONTALLY} instead
	 */
	@Deprecated
	CENTER_HORIZONTALLY("el-align-center-horizontally", true),
	
	/**
	 * Use {@link #ALIGN_CENTER_VERTICALLY} instead
	 */
	@Deprecated
	CENTER_VERTICALLY("el-align-center-vertically", true),
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
