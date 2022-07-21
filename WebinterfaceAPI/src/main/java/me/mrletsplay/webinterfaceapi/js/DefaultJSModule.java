package me.mrletsplay.webinterfaceapi.js;

import java.util.Locale;

public enum DefaultJSModule implements FileJSModule {

	BASE,
	TOAST,
	BASE_ACTIONS;

	@Override
	public String getFileName() {
		return name().toLowerCase(Locale.US) + ".js";
	}

	@Override
	public String getIdentifier() {
		return name().toLowerCase(Locale.US);
	}

}
