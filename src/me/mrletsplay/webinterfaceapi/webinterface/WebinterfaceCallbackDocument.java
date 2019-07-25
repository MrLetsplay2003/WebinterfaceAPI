package me.mrletsplay.webinterfaceapi.webinterface;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.header.HttpClientContentTypes;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;

public class WebinterfaceCallbackDocument implements HttpDocument {

	@Override
	public void createContent() {
		try {
			JSONObject req = (JSONObject) HttpRequestContext.getCurrentContext().getClientHeader().getPostData().getParsedAs(HttpClientContentTypes.JSON);
			WebinterfaceRequestEvent e = new WebinterfaceRequestEvent(req.getString("target"), req.getString("method"), req.get("data"));
			for(WebinterfaceActionHandler h : Webinterface.getActionHandlers()) {
				WebinterfaceResponse r = h.handle(e);
				if(r != null) {
					setResponse(r);
					return;
				}
			}
			setResponse(WebinterfaceResponse.error("No handler available"));
		}catch(Exception e) {
			e.printStackTrace();
			setResponse(WebinterfaceResponse.error("Failed to handle request"));
			return;
		}
	}
	
	private void setResponse(WebinterfaceResponse response) {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent("application/json", response.toJSON().toString().getBytes(StandardCharsets.UTF_8));
	}

}
