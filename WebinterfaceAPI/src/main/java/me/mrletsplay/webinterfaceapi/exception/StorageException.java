package me.mrletsplay.webinterfaceapi.exception;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = -5454054943127808352L;

	public StorageException() {
		super();
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageException(String message) {
		super(message);
	}

	public StorageException(Throwable cause) {
		super(cause);
	}

}
