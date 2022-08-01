package me.mrletsplay.webinterfaceapi.auth;

public class AuthException extends RuntimeException {

	private static final long serialVersionUID = -5361244211055931029L;

	public AuthException(String reason) {
		super(reason);
	}

	public AuthException(String reason, Throwable cause) {
		super(reason, cause);
	}

	public AuthException(Throwable cause) {
		super(cause);
	}

}
