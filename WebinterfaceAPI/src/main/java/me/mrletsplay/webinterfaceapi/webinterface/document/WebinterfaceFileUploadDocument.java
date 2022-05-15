package me.mrletsplay.webinterfaceapi.webinterface.document;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.header.PostData;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.form.FormData;
import me.mrletsplay.simplehttpserver.http.request.form.FormElement;
import me.mrletsplay.simplehttpserver.http.request.multipart.Multipart;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandlers;
import me.mrletsplay.webinterfaceapi.webinterface.session.Session;

public class WebinterfaceFileUploadDocument implements HttpDocument {

	@Override
	public void createContent() {
		try {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();

			Session sess = Session.getCurrentSession();
			if(sess == null) {
				setResponse(ActionResponse.error("No session active"));
				return;
			}

			PostData p = ctx.getClientHeader().getPostData();
			if(p == null) {
				setResponse(ActionResponse.error("No post data"));
				return;
			}

			Multipart mp = (Multipart) p.getParsed();
			FormData d = mp.toFormData();
			FormElement el = d.getElement("file");
			byte[] fileData = el.getData();
			String requestTarget = ctx.getRequestedPath().getQuery().getFirst("target");
			String requestMethod = ctx.getRequestedPath().getQuery().getFirst("method");
			JSONObject obj = new JSONObject();
			obj.put("value", Base64.getEncoder().encodeToString(fileData));
			ActionEvent event = new ActionEvent(null, sess, requestTarget, requestMethod, obj);
			setResponse(WebinterfaceActionHandlers.handle(event));
		}catch(Exception e) {
			Webinterface.getLogger().debug("Failed to handle request", e);
			setResponse(ActionResponse.error("Failed to handle request"));
			return;
		}
	}

	private void setResponse(ActionResponse response) {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent("application/json", response.toJSON().toString().getBytes(StandardCharsets.UTF_8));
	}

}
