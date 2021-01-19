package me.mrletsplay.webinterfaceapi.webinterface.js;

public enum DefaultJSModule implements WebinterfaceJSModule {
	
	BASE,
	TOAST,
	BASE_ACTIONS;
	
	@Override
	public String getFileName() {
		return name().toLowerCase() + ".js";
	}

}
