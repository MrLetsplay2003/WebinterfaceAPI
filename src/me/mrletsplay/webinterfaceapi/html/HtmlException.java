package me.mrletsplay.webinterfaceapi.html;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class HtmlException extends FriendlyException{

	private static final long serialVersionUID = 2452528897360595754L;

	public HtmlException(String reason, Throwable cause) {
		super(reason, cause);
	}

	public HtmlException(String reason) {
		super(reason);
	}

	public HtmlException(Throwable cause) {
		super(cause);
	}
	
}
