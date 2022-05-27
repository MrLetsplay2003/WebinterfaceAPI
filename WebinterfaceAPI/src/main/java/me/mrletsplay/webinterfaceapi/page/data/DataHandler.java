package me.mrletsplay.webinterfaceapi.page.data;

import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;

public class DataHandler {

	private String requestTarget;
	private String requestMethod;
	private ObjectValue data;
	private String key;

	private DataHandler(String requestTarget, String requestMethod, ObjectValue data, String key) {
		this.requestTarget = requestTarget;
		this.requestMethod = requestMethod;
		this.data = data;
		this.key = key;
	}

	public ObjectValue toObject() {
		ObjectValue obj = ActionValue.object();
		obj.put("requestTarget", ActionValue.string(requestTarget));
		obj.put("requestMethod", ActionValue.string(requestMethod));
		obj.put("data", data);
		obj.put("key", ActionValue.string(key));
		return obj;
	}

	public static DataHandler of(String requestTarget, String requestMethod) {
		return new DataHandler(requestTarget, requestMethod, null, null);
	}

	public static DataHandler of(String requestTarget, String requestMethod, ObjectValue data) {
		return new DataHandler(requestTarget, requestMethod, data, null);
	}

	public static DataHandler of(String requestTarget, String requestMethod, String key) {
		return new DataHandler(requestTarget, requestMethod, null, key);
	}

	public static DataHandler of(String requestTarget, String requestMethod, ObjectValue data, String key) {
		return new DataHandler(requestTarget, requestMethod, data, key);
	}

}
