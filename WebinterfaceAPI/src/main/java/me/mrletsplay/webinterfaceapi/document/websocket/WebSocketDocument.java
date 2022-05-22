package me.mrletsplay.webinterfaceapi.document.websocket;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.simplehttpserver.http.websocket.WebSocketConnection;
import me.mrletsplay.simplehttpserver.http.websocket.WebSocketEndpoint;
import me.mrletsplay.simplehttpserver.http.websocket.frame.CloseFrame;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.WebinterfaceActionHandlers;
import me.mrletsplay.webinterfaceapi.session.Session;

public class WebSocketDocument extends WebSocketEndpoint {

	@Override
	public void onTextMessage(WebSocketConnection connection, String message) {
		try {
			JSONObject o = new JSONObject(message);
			WebSocketData socketData = connection.getAttachment();
			Packet p = JSONConverter.decodeObject(o, Packet.class);
			if(socketData == null) {
				if(p.getRequestTarget() != null || p.getRequestMethod() != null) {
					connection.close(CloseFrame.POLICY_VIOLATION, "Not an init request");
					return;
				}

				JSONObject data = p.getData();
				if(!data.isOfType("sessionID", JSONType.STRING)) {
					connection.close(CloseFrame.POLICY_VIOLATION, "Invalid init packet");
					return;
				}

				String sessionID = data.getString("sessionID");
				Session sess = Webinterface.getSessionStorage().getSession(sessionID);
				if(sess == null) {
					connection.close(CloseFrame.POLICY_VIOLATION, "Session invalid");
					return;
				}

				connection.setAttachment(new WebSocketData(sess));
				return;
			}

			ActionEvent event = new ActionEvent(connection, socketData.getSession(), p.getRequestTarget(), p.getRequestMethod(), p.getData());
			ActionResponse response = WebinterfaceActionHandlers.handle(event);
			send(connection, p, response);
		}catch(Exception e) {
			Webinterface.getLogger().error("Recieved malformed request", e);
			connection.close(CloseFrame.POLICY_VIOLATION, "Malformed request");
		}
	}

	private void send(WebSocketConnection connection, Packet referrer, ActionResponse response) {
		connection.sendText(Packet.of(referrer.getID(), response).toJSON().toString());
	}

}
