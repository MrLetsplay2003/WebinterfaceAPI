package me.mrletsplay.webinterfaceapi.http.websocket.frame;

import java.util.Arrays;

public enum WebSocketOpCode {
	
	CONTINUATION_FRAME(0x0, false),
	TEXT_FRAME(0x1, false),
	BINARY_FRAME(0x2, false),
	// ... Reserved ...
	CONNECTION_CLOSE(0x8, true),
	PING(0x9, true),
	PONG(0xa, true),
	UNKNOWN(-1, false);
	
	private final int code;
	private final boolean control;
	
	private WebSocketOpCode(int code, boolean control) {
		this.code = code;
		this.control = control;
	}
	
	public int getCode() {
		return code;
	}
	
	public boolean isControl() {
		return control;
	}
	
	public static WebSocketOpCode getByCode(int code) {
		return Arrays.stream(values())
				.filter(o -> o.code == code)
				.findFirst().orElse(UNKNOWN);
	}

}
