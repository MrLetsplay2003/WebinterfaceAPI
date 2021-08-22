package me.mrletsplay.webinterfaceapi.http.websocket;

public class WebSocketException extends RuntimeException {

	private static final long serialVersionUID = -6731055125709470351L;

	public WebSocketException(Throwable cause) {
		super(cause);
	}
	
	public WebSocketException(String reason) {
		super(reason);
	}
	
	public WebSocketException(String reason, Throwable cause) {
		super(reason, cause);
	}

}
