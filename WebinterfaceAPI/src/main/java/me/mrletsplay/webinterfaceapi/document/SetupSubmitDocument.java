package me.mrletsplay.webinterfaceapi.document;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.header.DefaultClientContentTypes;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.util.MimeType;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.setup.SetupStep;

public class SetupSubmitDocument implements HttpDocument {

	@Override
	public void createContent() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		JSONObject data = ctx.getClientHeader().getPostData().getParsedAs(DefaultClientContentTypes.JSON_OBJECT);
		SetupStep step = Webinterface.getSetup().getNextStep();
		if(step == null) return;
		// TODO: verify data
		String r = step.callback(data);
		if(r != null) {
			error(r);
			return;
		}

		Webinterface.getSetup().addCompletedStep(step.getID());
	}

	private void error(String message) {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		ctx.getServerHeader().setStatusCode(HttpStatusCodes.BAD_REQUEST_400);
		ctx.getServerHeader().setContent(MimeType.TEXT, message.getBytes(StandardCharsets.UTF_8));
	}

}
