package me.mrletsplay.webinterfaceapi.http.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mrletsplay.webinterfaceapi.http.header.HttpClientHeader;
import me.mrletsplay.webinterfaceapi.http.header.HttpHeaderFields;
import me.mrletsplay.webinterfaceapi.http.request.form.FormData;
import me.mrletsplay.webinterfaceapi.http.request.form.FormElement;

public class Multipart {
	
	private List<MultipartBodyPart> bodyParts;
	
	private Multipart(List<MultipartBodyPart> bodyParts) {
		this.bodyParts = bodyParts;
	}
	
	public List<MultipartBodyPart> getBodyParts() {
		return bodyParts;
	}
	
	public FormData toFormData() {
		List<FormElement> elements = new ArrayList<>();
		for(MultipartBodyPart bp : bodyParts) {
			String disp = bp.getHeaders().getFieldValue("Content-Disposition");
			if(disp == null || !disp.startsWith("form-data;")) throw new IllegalStateException("Invalid form data");
			Map<String, String> props = new HashMap<>();
			for(String prop : disp.substring("form-data;".length()).trim().split(";")) {
				String[] propSpl = prop.trim().split("=");
				props.put(propSpl[0], propSpl[1]
						.substring(1, propSpl[1].length() - 1)
						.replace("\\\"", "\"")
						.replace("\\\\", "\\"));
			}
			elements.add(new FormElement(bp.getHeaders(), bp.getData(), props));
		}
		return new FormData(elements);
	}
	
	public static Multipart parse(HttpHeaderFields headers, byte[] postData) {
		String contentType = headers.getFieldValue("Content-Type");
		String[] spl = contentType.split(";");
		if(spl.length != 2) throw new IllegalArgumentException("Invalid multipart data");
		String[] boundarySpl = spl[1].trim().split("=");
		if(boundarySpl.length != 2) throw new IllegalArgumentException("Invalid multipart data");
		String boundary = boundarySpl[1].trim();
		String fullBoundary = "\r\n--" + boundary;
		int i = boundary.length() + 4; // Skip initial --boundaryCRLF
		List<MultipartBodyPart> bodyParts = new ArrayList<>();
		while(i < postData.length) {
			int headersEnd = nextIndexOf(postData, i, "\r\n\r\n");
			if(headersEnd == -1) break;
			headersEnd += 4;
			int nextB = nextIndexOf(postData, headersEnd, fullBoundary);
			if(nextB == -1) break;
			byte[] headerBytes = new byte[headersEnd - i];
			System.arraycopy(postData, i, headerBytes, 0, headerBytes.length);
			HttpHeaderFields bodyHeaders;
			try {
				bodyHeaders = HttpClientHeader.parseHeaders(new ByteArrayInputStream(headerBytes));
			} catch (IOException e) {
				throw new IllegalArgumentException("Failed to parse multipart headers", e);
			}
			byte[] bodyBytes = new byte[nextB - headersEnd];
			System.arraycopy(postData, headersEnd, bodyBytes, 0, bodyBytes.length);
			bodyParts.add(new MultipartBodyPart(bodyHeaders, bodyBytes));
			if(postData[nextB + fullBoundary.length()] == '-' && postData[nextB + fullBoundary.length() + 1] == '-') break;
			i = nextB + fullBoundary.length() + 2;
		}
		return new Multipart(bodyParts);
	}

	private static int nextIndexOf(byte[] array, int offset, String boundary) {
		byte[] strBytes = boundary.getBytes(StandardCharsets.UTF_8);
		for(int i = offset; i < array.length - boundary.length(); i++) {
			boolean found = true;
			for(int j = 0; j < boundary.length(); j++) {
				if(array[i + j] != strBytes[j]) {
					found = false;
				}
			}
			if(found) return i;
		}
		return -1;
	}
	
}
