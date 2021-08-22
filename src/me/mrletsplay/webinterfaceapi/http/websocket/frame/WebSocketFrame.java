package me.mrletsplay.webinterfaceapi.http.websocket.frame;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import me.mrletsplay.webinterfaceapi.http.websocket.WebSocketException;

public abstract class WebSocketFrame {
	
	protected static final int MAX_FRAME_SIZE = 65536;
	
	private boolean fin;
	
	private boolean
		rsv1,
		rsv2,
		rsv3;
	
	private WebSocketOpCode opCode;
	private byte[] payload;
	
	public WebSocketFrame(boolean fin, boolean rsv1, boolean rsv2, boolean rsv3, WebSocketOpCode opCode, byte[] payload) {
		this.fin = fin;
		this.rsv1 = rsv1;
		this.rsv2 = rsv2;
		this.rsv3 = rsv3;
		this.opCode = opCode;
		this.payload = payload;
	}

	public boolean isFin() {
		return fin;
	}
	
	public boolean isRSV1() {
		return rsv1;
	}
	
	public boolean isRSV2() {
		return rsv2;
	}
	
	public boolean isRSV3() {
		return rsv3;
	}
	
	public WebSocketOpCode getOpCode() {
		return opCode;
	}
	
	public byte[] getPayload() {
		return payload;
	}
	
	public void appendPayload(byte[] additionalPayload) {
		long newLength = (long) payload.length + additionalPayload.length;
		if(newLength > Integer.MAX_VALUE) throw new WebSocketException("Concatenated frame too big: " + newLength + " > " + Integer.MAX_VALUE);
		byte[] newPayload = new byte[(int) newLength];
		System.arraycopy(payload, 0, newPayload, 0, payload.length);
		System.arraycopy(additionalPayload, 0, newPayload, payload.length, additionalPayload.length);
		this.payload = newPayload;
	}
	
	public void write(OutputStream out) throws IOException {
		int b1 = (fin ? 1 : 0) << 7
				| (rsv1 ? 1 : 0) << 6
				| (rsv2 ? 1 : 0) << 5
				| (rsv3 ? 1 : 0) << 4
				| opCode.getCode();
		out.write(b1);
		
		if(payload.length > 65535) {
			out.write(127);
			out.write(0); // First four bytes are all 0, because max payload size is Integer.MAX_VALUE
			out.write(0);
			out.write(0);
			out.write(0);
			out.write(payload.length >> 24);
			out.write((payload.length >> 16) & 0xFF);
			out.write((payload.length >> 8) & 0xFF);
			out.write(payload.length & 0xFF);
		}else if(payload.length > 126) {
			out.write(126);
			out.write((payload.length >> 8) & 0xFF);
			out.write(payload.length & 0xFF);
		}else {
			out.write(payload.length);
		}

		out.write(payload);
		out.flush();
	}
	
	public abstract WebSocketFrame[] split();

	public static WebSocketFrame read(InputStream in) throws IOException {
		int b1 = in.read();
		if(b1 == -1) throw new InvalidFrameException("Unexpected end of stream");
		boolean fin = (b1 & 0x80) != 0;
		boolean rsv1 = (b1 & 0x40) != 0;
		boolean rsv2 = (b1 & 0x20) != 0;
		boolean rsv3 = (b1 & 0x10) != 0;
		int rawOpCode = b1 & 0x0F;
		WebSocketOpCode opCode = WebSocketOpCode.getByCode(rawOpCode);
		if(opCode == WebSocketOpCode.UNKNOWN) throw new InvalidFrameException("Unknown opcode: " + rawOpCode);
		if(opCode.isControl() && !fin) throw new InvalidFrameException("Control frames can't be fragmented");
		
		int b2 = in.read();
		if(b2 == -1) throw new InvalidFrameException("Unexpected end of stream");
		boolean mask = (b2 & 0x80) != 0;
		if(!mask) throw new InvalidFrameException("Client-to-server frames must be masked");
		int payloadLength = b2 & 0x7F;
		
		if(payloadLength == 126) {
			int pL = in.read();
			int pL2 = in.read();
			if(pL == -1 || pL2 == -1) throw new InvalidFrameException("Unexpected end of stream");
			
			payloadLength = (pL << 8) | pL2;
		}else if(payloadLength == 127) {
			byte[] pL = new byte[8];
			int r = in.read(pL);
			if(r < 8) throw new InvalidFrameException("Unexpected end of stream");
			// Note: Skipping most significant bit check, because that would result in a number greater than Integer.MAX_VALUE anyway
			BigInteger p = new BigInteger(1, pL);
			if(p.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) == 1) throw new InvalidFrameException("Frame too big: " + p.toString() + " > " + Integer.MAX_VALUE);
			payloadLength = p.intValue();
		}
		
		byte[] maskingKey = new byte[4];
		int mKL = in.read(maskingKey);
		if(mKL < 4) throw new InvalidFrameException("Unexpected end of stream");
		
		byte[] payload = new byte[payloadLength];
		
		int lenRead = 0;
		int len;
		while((len = in.read(payload, lenRead, payloadLength - lenRead)) > 0) {
			lenRead += len;
		}
		if(lenRead < payloadLength) throw new InvalidFrameException("Unexpected end of stream");
		
		for(int i = 0; i < payloadLength; i++) {
			payload[i] = (byte) (payload[i] ^ maskingKey[i % 4]);
		}
		
		switch(opCode) {
			case BINARY_FRAME:
				return new BinaryFrame(fin, rsv1, rsv2, rsv3, payload);
			case CONNECTION_CLOSE:
				return new CloseFrame(fin, rsv1, rsv2, rsv3, payload);
			case CONTINUATION_FRAME:
				return new ContinuationFrame(fin, rsv1, rsv2, rsv3, payload);
			case PING:
				return new PingFrame(fin, rsv1, rsv2, rsv3, payload);
			case PONG:
				return new PongFrame(fin, rsv1, rsv2, rsv3, payload);
			case TEXT_FRAME:
				return new TextFrame(fin, rsv1, rsv2, rsv3, payload);
			default:
				throw new InvalidFrameException("Unsupported opcode: " + opCode);
		}
	}
	
}
