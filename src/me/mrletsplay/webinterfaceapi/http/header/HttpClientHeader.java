package me.mrletsplay.webinterfaceapi.http.header;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class HttpClientHeader {

	private String method;
	private HttpURLPath path;
	private String protocolVersion;
	private HttpHeaderFields fields;
	private byte[] postData;
	
	public HttpClientHeader(String method, HttpURLPath path, String protocolVersion, HttpHeaderFields fields, byte[] postData) {
		this.method = method;
		this.path = path;
		this.protocolVersion = protocolVersion;
		this.fields = fields;
		this.postData = postData;
	}
	
	public String getMethod() {
		return method;
	}
	
	public HttpURLPath getPath() {
		return path;
	}
	
	public String getProtocolVersion() {
		return protocolVersion;
	}
	
	public HttpHeaderFields getFields() {
		return fields;
	}
	
	public PostData getPostData() {
		return new PostData(fields.getFieldValue("Content-Type"), postData);
	}
	
	public static HttpClientHeader parse(InputStream data) {
		try {
			String reqLine = readLine(data);
			if(reqLine == null) return null;
			String[] fs = reqLine.split(" ");
			String method = fs[0];
			HttpURLPath path = HttpURLPath.parse(fs[1]);
			String protocolVersion = fs[2];
			HttpHeaderFields fields = new HttpHeaderFields();
			String l;
			while((l = readLine(data)) != null && !l.isEmpty()) {
				if(l.isEmpty()) break;
				String[] kv = l.split(": ", 2);
				fields.addFieldValue(kv[0], kv[1]);
			}
			if(l == null) {
				return null;
			}
			String cL = fields.getFieldValue("Content-Length");
			byte[] postData = new byte[0];
			if(cL != null) {
				int contLength;
				try {
					contLength = Integer.parseInt(fields.getFieldValue("Content-Length"));
				}catch(Exception e) {
					return null;
				}
				postData = new byte[contLength];
				int actualLen = data.read(postData);
				int tries = 0;
				while(actualLen != contLength) {
					if(tries++ >= 10) return null; // Give up after 10 tries
					actualLen += data.read(postData, actualLen, contLength - actualLen); // Retry reading remaining bytes until socket times out
				}
			}
			return new HttpClientHeader(method, path, protocolVersion, fields, postData);
		}catch(IOException e) {
			return null;
		}
	}
	
	private static String readLine(InputStream in) throws IOException {
		StringBuilder b = new StringBuilder();
		int c;
		while((c = in.read()) != '\r' && c != -1) {
			b.appendCodePoint(c);
		}
		if(c == -1) return null;
		in.read();
		return b.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof HttpClientHeader)) return false;
		HttpClientHeader o = (HttpClientHeader) obj;
		return method.equals(o.method)
				&& path.equals(o.path)
				&& protocolVersion.equals(o.protocolVersion)
				&& fields.equals(o.fields)
				&& Arrays.equals(postData, o.postData);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(method, path, protocolVersion, fields, Arrays.hashCode(postData));
	}
	
}
