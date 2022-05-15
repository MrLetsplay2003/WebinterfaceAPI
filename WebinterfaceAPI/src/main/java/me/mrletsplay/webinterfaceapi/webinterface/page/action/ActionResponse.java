package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.mrcore.json.JSONObject;

public class ActionResponse {

	private boolean success;
	private JSONObject data;
	private String errorMessage;
	
	private ActionResponse(boolean success, JSONObject data, String errorMessage) {
		this.success = success;
		this.data = data;
		this.errorMessage = errorMessage;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public JSONObject getData() {
		return data;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		o.put("success", success);
		if(success) {
			o.put("data", data);
		}else {
			o.put("message", errorMessage);
		}
		return o;
	}
	
	public static ActionResponse success(JSONObject data) {
		return new ActionResponse(true, data, null);
	}
	
	public static ActionResponse success() {
		return success(null);
	}
	
	public static ActionResponse error(String message) {
		return new ActionResponse(false, null, message);
	}
	
}
