package me.mrletsplay.webinterfaceapi.php;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCode;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;

public class PHPFileDocument implements HttpDocument {
	
	private File file;
	
	public PHPFileDocument(File file) {
		this.file = file;
	}
	
	public PHPFileDocument(File file, String mimeType) {
		this.file = file;
	}
	
	@Override
	public void createContent() {
		HttpRequestContext c = HttpRequestContext.getCurrentContext();
		if(!PHP.isEnabled()) {
			c.getServerHeader().setContent("text/plain", "PHP is disabled".getBytes(StandardCharsets.UTF_8));
			return;
		}
		
		List<String> cmd = new ArrayList<>();
		cmd.add(PHP.getCGIPath());
		
		ProcessBuilder b = new ProcessBuilder(cmd);
		b.redirectError(Redirect.INHERIT);
		
		Map<String, String> env = b.environment();
		env.put("REDIRECT_STATUS", "CGI");
		env.put("REQUEST_METHOD", c.getClientHeader().getMethod());
		env.put("SCRIPT_NAME", c.getClientHeader().getPath().getDocumentPath());
		env.put("SCRIPT_FILENAME", file.toPath().toAbsolutePath().normalize().toString());
		env.put("REQUEST_URI", c.getClientHeader().getPath().getDocumentPath());
		env.put("QUERY_STRING", c.getClientHeader().getPath().getGetParameters().entrySet().stream()
				.map(e -> HttpUtils.urlEncode(e.getKey()) + "=" + HttpUtils.urlEncode(e.getValue().stream().collect(Collectors.joining())))
				.collect(Collectors.joining("&")));
		if(c.getClientHeader().getMethod().equals("POST")
				&& !c.getClientHeader().getFields().getFieldValues("Content-Type").isEmpty()) {
				env.put("CONTENT_TYPE", c.getClientHeader().getFields().getFieldValue("Content-Type"));
		}
		env.put("CONTENT_LENGTH", ""+c.getClientHeader().getPostData().getRaw().length);
		
		c.getClientHeader().getFields().getFields().forEach((k, v) -> {
			env.put("HTTP_" + k.replace("-", "_").toUpperCase(), v.stream().collect(Collectors.joining("; ")));
		});
		
		env.put("SERVER_NAME", "localhost");
		env.put("SERVER_ADDR", "127.0.0.1");
		env.put("SERVER_PORT", "80");
		env.put("GATEWAY_INTERFACE", "CGI/1.1");
		env.put("SERVER_PROTOCOL", "HTTP/1.1");
		String pth = Paths.get("").toAbsolutePath().toString();
		env.put("DOCUMENT_ROOT", pth);
		
		try {
			System.out.println("RUNNING PHP-CGI: " + file.getName());
			Process p = b.start();
			ByteArrayInputStream bin = new ByteArrayInputStream(c.getClientHeader().getPostData().getRaw());
			IOUtils.transfer(bin, p.getOutputStream());
			p.getOutputStream().flush();
			bin.close();
			
			byte[] data = IOUtils.readAllBytes(p.getInputStream());
			
			String dt = new String(data, StandardCharsets.UTF_8);
			dt = dt.substring(0, dt.indexOf("\r\n\r\n"));
			
			int prDtLen = dt.getBytes(StandardCharsets.UTF_8).length;
			byte[] postData = new byte[data.length - prDtLen - 4]; // - 4 bytes because of the \r\n\r\n
			
			System.arraycopy(data, prDtLen + 4, postData, 0, postData.length);

			for(String h : dt.split("\r\n")) {
				String[] spl = h.split(": ", 2);
				if(spl[0].equalsIgnoreCase("Status")) {
					c.getServerHeader().setStatusCode(new HttpStatusCode() {
						
						@Override
						public String getStatusMessage() {
							return spl[1].split(" ", 2)[1];
						}
						
						@Override
						public int getStatusCode() {
							return Integer.parseInt(spl[1].split(" ", 2)[0]);
						}
					});
					continue;
				}
				if(spl[0].equalsIgnoreCase("Content-Type")) {
					c.getServerHeader().getFields().setFieldValue(spl[0], spl[1]); // Always *override* content type
					continue;
				}
				c.getServerHeader().getFields().addFieldValue(spl[0], spl[1]);
			}
			c.getServerHeader().setContent("text/html", postData, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
