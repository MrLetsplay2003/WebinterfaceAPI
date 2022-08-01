package me.mrletsplay.webinterfaceapi.exception;

public class WebinterfaceInitException extends RuntimeException {

	private static final long serialVersionUID = -8268299693172172290L;

	public WebinterfaceInitException() {
		super();
	}

	public WebinterfaceInitException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebinterfaceInitException(String message) {
		super(message);
	}

	public WebinterfaceInitException(Throwable cause) {
		super(cause);
	}

}
