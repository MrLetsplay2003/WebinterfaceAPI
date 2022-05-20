package me.mrletsplay.webinterfaceapi.js;

public enum DefaultJSModule implements FileJSModule {
	
	BASE,
	TOAST,
	BASE_ACTIONS;
	
	@Override
	public String getFileName() {
		return name().toLowerCase() + ".js";
	}

	@Override
	public String getIdentifier() {
		return name().toLowerCase();
	}

}
