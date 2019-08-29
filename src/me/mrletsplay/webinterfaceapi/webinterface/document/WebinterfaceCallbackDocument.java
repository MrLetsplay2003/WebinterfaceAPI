package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.header.HttpClientContentTypes;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;

public class WebinterfaceCallbackDocument implements HttpDocument {

	@Override
	public void createContent() {
		try {
			JSONObject req = (JSONObject) HttpRequestContext.getCurrentContext().getClientHeader().getPostData().getParsedAs(HttpClientContentTypes.JSON);
			if(!req.isOfType("target", JSONType.STRING) || !req.isOfType("method", JSONType.STRING)) {
				setResponse(WebinterfaceResponse.error("Invalid request target or method, not a string"));
				return;
			}
			if(!req.isOfType("data", JSONType.OBJECT)) {
				setResponse(WebinterfaceResponse.error("Invalid data payload, not an object"));
				return;
			}
			WebinterfaceRequestEvent e = new WebinterfaceRequestEvent(req.getString("target"), req.getString("method"), req.getJSONObject("data"));
			for(WebinterfaceActionHandler h : Webinterface.getActionHandlers()) {
				try {
					WebinterfaceResponse r = h.handle(e);
					if(r != null) {
						setResponse(r);
						return;
					}
				}catch(Exception ex) {
					ex.printStackTrace(); // TODO: remove
					setResponse(WebinterfaceResponse.error("Failed to handle request, error at handler"));
					return;
				}
			}
			setResponse(WebinterfaceResponse.error("No handler available"));
		}catch(Exception e) {
			System.out.println("DEBUG STACK TRACE:"); // TODO remove
			e.printStackTrace();
			setResponse(WebinterfaceResponse.error("Failed to handle request"));
			return;
		}
	}
	
	private void setResponse(WebinterfaceResponse response) {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent("application/json", response.toJSON().toString().getBytes(StandardCharsets.UTF_8));
	}

}
