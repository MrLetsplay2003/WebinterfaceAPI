package me.mrletsplay.webinterfaceapi.webinterface.document.websocket;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.webinterfaceapi.http.websocket.WebSocketConnection;
import me.mrletsplay.webinterfaceapi.http.websocket.WebSocketEndpoint;
import me.mrletsplay.webinterfaceapi.http.websocket.frame.CloseFrame;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandlers;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfaceWebSocketDocument extends WebSocketEndpoint {
	
	@Override
	public void onTextMessage(WebSocketConnection connection, String message) {
		try {
			JSONObject o = new JSONObject(message);
			WebinterfaceSession session = connection.getAttachment();
			Packet p = JSONConverter.decodeObject(o, Packet.class);
			if(session == null) {
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
				WebinterfaceSession sess = Webinterface.getSessionStorage().getSession(sessionID);
				if(sess == null) {
					connection.close(CloseFrame.POLICY_VIOLATION, "Session invalid");
					return;
				}
				
				connection.setAttachment(sess);
			}
			
			WebinterfaceRequestEvent event = new WebinterfaceRequestEvent(session, p.getRequestTarget(), p.getRequestMethod(), p.getData());
			WebinterfaceResponse response = WebinterfaceActionHandlers.handle(event);
			send(connection, p, response);
		}catch(Exception e) {
			connection.close(CloseFrame.POLICY_VIOLATION, "Malformed request");
		}
	}
	
	private void send(WebSocketConnection connection, Packet referrer, WebinterfaceResponse response) {
		connection.sendText(Packet.of(referrer.getID(), response).toJSON().toString());
	}

}
