package me.mrletsplay.webinterfaceapi.page.element.builder;

import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;

public enum Align {
	
	TOP_LEFT(DefaultLayoutOption.ALIGN_TOP, DefaultLayoutOption.ALIGN_LEFT),
	TOP_CENTER(DefaultLayoutOption.ALIGN_TOP, DefaultLayoutOption.ALIGN_CENTER_HORIZONTALLY),
	TOP_RIGHT(DefaultLayoutOption.ALIGN_TOP, DefaultLayoutOption.ALIGN_RIGHT),
	LEFT_CENTER(DefaultLayoutOption.ALIGN_LEFT, DefaultLayoutOption.ALIGN_CENTER_VERTICALLY),
	CENTER(DefaultLayoutOption.ALIGN_CENTER_HORIZONTALLY, DefaultLayoutOption.ALIGN_CENTER_VERTICALLY),
	RIGHT_CENTER(DefaultLayoutOption.ALIGN_RIGHT, DefaultLayoutOption.ALIGN_CENTER_VERTICALLY),
	BOTTOM_LEFT(DefaultLayoutOption.ALIGN_BOTTOM, DefaultLayoutOption.ALIGN_LEFT),
	BOTTOM_CENTER(DefaultLayoutOption.ALIGN_BOTTOM, DefaultLayoutOption.ALIGN_CENTER_HORIZONTALLY),
	BOTTOM_RIGHT(DefaultLayoutOption.ALIGN_BOTTOM, DefaultLayoutOption.ALIGN_RIGHT);
	
	private DefaultLayoutOption[] layoutOptions;

	private Align(DefaultLayoutOption... layoutOptions) {
		this.layoutOptions = layoutOptions;
	}
	
	public DefaultLayoutOption[] getLayoutOptions() {
		return layoutOptions;
	}

}
