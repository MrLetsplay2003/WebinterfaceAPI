package me.mrletsplay.webinterfaceapi.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.header.DefaultClientContentTypes;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.WebinterfaceActionHandlers;
import me.mrletsplay.webinterfaceapi.session.Session;

public class ActionCallbackDocument implements HttpDocument {

	@Override
	public void createContent() {
		try {
			JSONObject req = HttpRequestContext.getCurrentContext().getClientHeader().getPostData().getParsedAs(DefaultClientContentTypes.JSON_OBJECT);
			if(!req.isOfType("target", JSONType.STRING) || !req.isOfType("method", JSONType.STRING)) {
				setResponse(ActionResponse.error("Invalid request target or method, not a string"));
				return;
			}

			if(!req.isOfType("data", JSONType.OBJECT)) {
				setResponse(ActionResponse.error("Invalid data payload, not an object"));
				return;
			}

			Session sess = Session.getCurrentSession();
			if(sess == null) {
				setResponse(ActionResponse.error("No session active"));
				return;
			}

			ActionEvent e = new ActionEvent(null, sess, req.getString("target"), req.getString("method"), req.getJSONObject("data"));
			setResponse(WebinterfaceActionHandlers.handle(e));
		}catch(Exception e) {
			Webinterface.getLogger().error("Failed to handle request", e);
			setResponse(ActionResponse.error("Failed to handle request"));
			return;
		}
	}

	private void setResponse(ActionResponse response) {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent("application/json", response.toJSON().toString().getBytes(StandardCharsets.UTF_8));
	}

}
