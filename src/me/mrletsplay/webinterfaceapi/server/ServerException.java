package me.mrletsplay.webinterfaceapi.server;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class ServerException extends FriendlyException {

	private static final long serialVersionUID = -2078638940629606435L;

	public ServerException(String reason, Throwable cause) {
		super(reason, cause);
	}

	public ServerException(String reason) {
		super(reason);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}

}
