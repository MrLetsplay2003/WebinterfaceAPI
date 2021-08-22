package me.mrletsplay.webinterfaceapi.http.websocket.frame;

import me.mrletsplay.webinterfaceapi.http.websocket.WebSocketException;

public class InvalidFrameException extends WebSocketException {
	
	private static final long serialVersionUID = 3511643317469908805L;

	public InvalidFrameException(String message) {
		super(message);
	}

}
