package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.header.PostData;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.http.request.Multipart;
import me.mrletsplay.webinterfaceapi.http.request.form.FormData;
import me.mrletsplay.webinterfaceapi.http.request.form.FormElement;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandlers;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceFileUploadDocument implements HttpDocument {
	
	@Override
	public void createContent() {
		try {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			
			WebinterfaceSession sess = WebinterfaceSession.getCurrentSession();
			if(sess == null) {
				setResponse(WebinterfaceResponse.error("No session active"));
				return;
			}
			
			PostData p = ctx.getClientHeader().getPostData();
			if(p == null) {
				setResponse(WebinterfaceResponse.error("No post data"));
				return;
			}
			
			Multipart mp = (Multipart) p.getParsed();
			FormData d = mp.toFormData();
			FormElement el = d.getElement("file");
			byte[] fileData = el.getData();
			String requestTarget = ctx.getClientHeader().getPath().getQueryParameterValue("target");
			String requestMethod = ctx.getClientHeader().getPath().getQueryParameterValue("method");
			JSONObject obj = new JSONObject();
			obj.put("value", Base64.getEncoder().encodeToString(fileData));
			WebinterfaceRequestEvent event = new WebinterfaceRequestEvent(null, sess, requestTarget, requestMethod, obj);
			setResponse(WebinterfaceActionHandlers.handle(event));
		}catch(Exception e) {
			Webinterface.getLogger().debug("Failed to handle request", e);
			setResponse(WebinterfaceResponse.error("Failed to handle request"));
			return;
		}
	}
	
	private void setResponse(WebinterfaceResponse response) {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent("application/json", response.toJSON().toString().getBytes(StandardCharsets.UTF_8));
	}

}
