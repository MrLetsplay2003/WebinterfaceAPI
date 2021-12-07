package me.mrletsplay.webinterfaceapi.webinterface.document.websocket;

import java.util.UUID;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;

public class Packet implements JSONConvertible {
	
	@JSONValue
	private String id;

	@JSONValue
	private String referrerID;

	@JSONValue
	private String requestTarget;

	@JSONValue
	private String requestMethod;

	@JSONValue
	private boolean success;

	@JSONValue
	private String errorMessage;
	
	@JSONValue
	private JSONObject data;
	
	@JSONConstructor
	public Packet() {}

	private Packet(String id, String referrerID, String requestTarget, String requestMethod, boolean success, String errorMessage, JSONObject data) {
		this.id = id;
		this.referrerID = referrerID;
		this.requestTarget = requestTarget;
		this.requestMethod = requestMethod;
		this.success = success;
		this.errorMessage = errorMessage;
		this.data = data;
	}
	
	public String getID() {
		return id;
	}

	public String getReferrerID() {
		return referrerID;
	}

	public String getRequestTarget() {
		return requestTarget;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public JSONObject getData() {
		return data;
	}

	public static Packet of(String requestTarget, String requestMethod, JSONObject data) {
		return new Packet(UUID.randomUUID().toString(), null, requestTarget, requestMethod, true, null, data);
	}
	
	public static Packet of(String referrerID, JSONObject data) {
		return new Packet(UUID.randomUUID().toString(), referrerID, null, null, true, null, data);
	}
	
	public static Packet ofError(String referrerID, String errorMessage) {
		return new Packet(UUID.randomUUID().toString(), referrerID, null, null, false, errorMessage, null);
	}
	
	public static Packet of(String referrerID, WebinterfaceResponse response) {
		return response.isSuccess() ? of(referrerID, response.getData()) : ofError(referrerID, response.getErrorMessage());
	}

}
