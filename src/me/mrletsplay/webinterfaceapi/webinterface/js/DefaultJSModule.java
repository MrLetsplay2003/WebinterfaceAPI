package me.mrletsplay.webinterfaceapi.webinterface.js;

public enum DefaultJSModule implements WebinterfaceFileJSModule {
	
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
