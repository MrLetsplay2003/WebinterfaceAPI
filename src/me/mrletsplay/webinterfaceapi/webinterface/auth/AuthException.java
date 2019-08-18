package me.mrletsplay.webinterfaceapi.webinterface.auth;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class AuthException extends FriendlyException {

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
