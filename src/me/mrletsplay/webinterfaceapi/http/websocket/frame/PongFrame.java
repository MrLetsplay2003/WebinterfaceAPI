package me.mrletsplay.webinterfaceapi.http.websocket.frame;

public class PongFrame extends WebSocketFrame {
	
	public PongFrame(boolean fin, boolean rsv1, boolean rsv2, boolean rsv3, byte[] payload) {
		super(fin, rsv1, rsv2, rsv3, WebSocketOpCode.PONG, payload);
	}
	
	@Override
	public WebSocketFrame[] split() {
		return new WebSocketFrame[] {this};
	}
	
}
