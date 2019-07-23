package me.mrletsplay.webinterfaceapi.http;

public enum HttpStatusCodes implements HttpStatusCode {
	
	OK_200(200, "OK"),
	NOT_FOUND_404(404, "Not Found"),
	;
	
	private final int code;
	private final String msg;
	
	private HttpStatusCodes(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public int getStatusCode() {
		return code;
	}
	
	@Override
	public String getStatusMessage() {
		return msg;
	}

}
