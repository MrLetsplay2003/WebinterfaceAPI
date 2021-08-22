package me.mrletsplay.webinterfaceapi.http.websocket;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import me.mrletsplay.webinterfaceapi.http.HttpConnection;
import me.mrletsplay.webinterfaceapi.http.websocket.frame.BinaryFrame;
import me.mrletsplay.webinterfaceapi.http.websocket.frame.CloseFrame;
import me.mrletsplay.webinterfaceapi.http.websocket.frame.PongFrame;
import me.mrletsplay.webinterfaceapi.http.websocket.frame.TextFrame;
import me.mrletsplay.webinterfaceapi.http.websocket.frame.WebSocketFrame;
import me.mrletsplay.webinterfaceapi.http.websocket.frame.WebSocketOpCode;

public class WebSocketConnection {
	
	private HttpConnection httpConnection;
	private WebSocketEndpoint endpoint;
	
	private boolean closed;
	
	private WebSocketFrame incompleteFrame;
	
	public WebSocketConnection(HttpConnection httpConnection, WebSocketEndpoint endpoint) {
		this.httpConnection = httpConnection;
		this.endpoint = endpoint;
	}

	public HttpConnection getHttpConnection() {
		return httpConnection;
	}
	
	public WebSocketEndpoint getEndpoint() {
		return endpoint;
	}
	
	public void send(WebSocketFrame frame) {
		if(closed) throw new WebSocketException("Connection is closed");
		try {
			for(WebSocketFrame f : frame.split()) {
				f.write(httpConnection.getSocket().getOutputStream());
			}
			httpConnection.getSocket().getOutputStream().flush();
		}catch(IOException e) {
			throw new WebSocketException("Failed to send frame", e);
		}
	}
	
	public void sendText(String message) {
		send(new TextFrame(true, false, false, false, message.getBytes(StandardCharsets.UTF_8)));
	}
	
	public void sendBinary(byte[] bytes) {
		send(new BinaryFrame(true, false, false, false, bytes));
	}
	
	public void close() {
		closed = true;
		httpConnection.close();
	}
	
	public void close(int code, String reason) {
		if(reason == null) {
			send(CloseFrame.of(code));
		}else {
			send(CloseFrame.of(code, reason));
		}
		
		close();
	}
	
	public void close(int code) {
		close(code, null);
	}
	
	public void receive() throws IOException {
		WebSocketFrame frame;
		frame = WebSocketFrame.read(httpConnection.getSocket().getInputStream());
		
		endpoint.onFrameReceived(this, frame);
		
		if(frame.getOpCode().isControl()) {
			switch(frame.getOpCode()) {
				case CONNECTION_CLOSE:
					close();
					endpoint.onClose(this, (CloseFrame) frame);
					return;
				case PING:
					endpoint.onPing(this, frame.getPayload());
					send(new PongFrame(true, false, false, false, new byte[0]));
					return;
				case PONG:
					endpoint.onPong(this, frame.getPayload());
					return;
				default:
					throw new WebSocketException("Unsupported control frame: " + frame.getOpCode());
			}
		}
		
		if(incompleteFrame != null) {
			if(frame.getOpCode() != WebSocketOpCode.CONTINUATION_FRAME) throw new WebSocketException("Interleaving of message frames is not allowed");
			incompleteFrame.appendPayload(frame.getPayload());
			if(frame.isFin()) {
				handleCompleteFrame(frame);
				incompleteFrame = null;
			}
			return;
		}
		
		if(frame.getOpCode() == WebSocketOpCode.CONTINUATION_FRAME) throw new WebSocketException("Received continuation frame without prior incomplete frame");
		
		if(!frame.isFin()) {
			incompleteFrame = frame;
			return;
		}
		
		handleCompleteFrame(frame);
	}
	
	private void handleCompleteFrame(WebSocketFrame frame) {
		endpoint.onCompleteFrameReceived(this, frame);
		
		switch(frame.getOpCode()) {
			case BINARY_FRAME:
				endpoint.onBinaryMessage(this, frame.getPayload());
				break;
			case TEXT_FRAME:
				endpoint.onTextMessage(this, ((TextFrame) frame).getText());
				break;
			default:
				break;
		}
	}
	
}
