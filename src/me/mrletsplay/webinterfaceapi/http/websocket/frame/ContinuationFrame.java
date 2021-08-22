package me.mrletsplay.webinterfaceapi.http.websocket.frame;

public class ContinuationFrame extends WebSocketFrame {
	
	public ContinuationFrame(boolean fin, boolean rsv1, boolean rsv2, boolean rsv3, byte[] payload) {
		super(fin, rsv1, rsv2, rsv3, WebSocketOpCode.CONTINUATION_FRAME, payload);
	}
	
	@Override
	public WebSocketFrame[] split() {
		throw new UnsupportedOperationException("Can't split a continuation frame");
	}
	
}
