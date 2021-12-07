package me.mrletsplay.webinterfaceapi.http.websocket.frame;

import java.nio.charset.StandardCharsets;

public class CloseFrame extends WebSocketFrame {
	
	public static final int
		NORMAL_CLOSURE = 1000,
		GOING_AWAY = 1001,
		PROTOCOL_ERROR = 1002,
		UNSUPPORTED_DATA = 1003,
		NO_STATUS_RECEIVED = 1005,
		ABNORMAL_CLOSURE = 1006,
		INVALID_FRAME_PAYLOAD_DATA = 1007,
		POLICY_VIOLATION = 1008,
		MESSAGE_TOO_BIG = 1009,
		MISSING_EXTENSION = 1010,
		INTERNAL_ERROR = 1011,
		SERVICE_RESTART = 1012,
		TRY_AGAIN_LATER = 1013,
		BAD_GATEWAY = 1014,
		TLS_HANDSHAKE = 1015;
	
	private int code;
	private String reason;
	
	public CloseFrame(boolean fin, boolean rsv1, boolean rsv2, boolean rsv3, byte[] payload) {
		super(fin, rsv1, rsv2, rsv3, WebSocketOpCode.CONNECTION_CLOSE, payload);
		
		if(payload.length > 0) {
			if(payload.length < 2) throw new InvalidFrameException("Invalid payload length for close frame");
			code = ((payload[0] & 0xFF) << 8) | (payload[1] & 0xFF);
			
			if(payload.length > 2) reason = new String(payload, 2, payload.length - 2, StandardCharsets.UTF_8);
		}
	}
	
	public int getCode() {
		return code;
	}
	
	public String getReason() {
		return reason;
	}
	
	@Override
	public WebSocketFrame[] split() {
		return new WebSocketFrame[] {this};
	}
	
	public static CloseFrame of(int code, String reason) {
		byte[] bs = reason.getBytes(StandardCharsets.UTF_8);
		byte[] payload = new byte[bs.length + 2];
		payload[0] = (byte) ((code >> 8) & 0xFF);
		payload[1] = (byte) (code & 0xFF);
		System.arraycopy(bs, 0, payload, 2, bs.length);
		return new CloseFrame(true, false, false, false, payload);
	}
	
	public static CloseFrame of(int code) {
		byte[] payload = new byte[2];
		payload[0] = (byte) ((code >> 8) & 0xFF);
		payload[1] = (byte) (code & 0xFF);
		return new CloseFrame(true, false, false, false, payload);
	}
	
}
