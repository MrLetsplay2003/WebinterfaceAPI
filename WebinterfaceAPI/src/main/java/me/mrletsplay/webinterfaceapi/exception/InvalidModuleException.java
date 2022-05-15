package me.mrletsplay.webinterfaceapi.exception;

public class InvalidModuleException extends RuntimeException {

	private static final long serialVersionUID = 2547748691076481853L;
	
	public InvalidModuleException(String moduleIdentifier, Throwable t) {
		super(String.format("Invalid module \"%s\"", moduleIdentifier), t);
	}
	
	public InvalidModuleException(String moduleIdentifier) {
		this(moduleIdentifier, null);
	}

}
