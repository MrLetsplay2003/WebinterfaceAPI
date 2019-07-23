package me.mrletsplay.webinterfaceapi.http;

public enum HttpProtocolVersions implements HttpProtocolVersion {
	
	HTTP1_1("HTTP/1.1");

	private final String verStr;
	
	private HttpProtocolVersions(String verStr) {
		this.verStr = verStr;
	}
	
	@Override
	public String getVersionString() {
		return verStr;
	}

}
