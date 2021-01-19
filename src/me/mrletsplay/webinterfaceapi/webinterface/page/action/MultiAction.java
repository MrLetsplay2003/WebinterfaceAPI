package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.WebinterfaceJSModule;

public class MultiAction implements WebinterfaceAction {

	private List<WebinterfaceAction> actions;
	
	public MultiAction(List<WebinterfaceAction> actions) {
		this.actions = actions;
	}
	
	public MultiAction(WebinterfaceAction... actions) {
		this(Arrays.asList(actions));
	}
	
//	@Override
//	public JavaScriptFunction toJavaScript() {
//		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
//		StringBuilder code = new StringBuilder();
//		for(WebinterfaceAction ac : actions) {
//			JavaScriptFunction fc = ac.toJavaScript();
//			code.append(fc.getCode().get());
//		}
//		f.setCode(code.toString());
//		return f;
//	}
	
	@Override
	public String getHandlerName() {
		return "WebinterfaceBaseActions.multiAction";
	}
	
	@Override
	public JSONObject getParameters() {
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();
		for(WebinterfaceAction ac : actions) {
			JSONObject j = new JSONObject();
			j.put("name", ac.getHandlerName());
			j.put("parameters", ac.getParameters());
			a.add(j);
		}
		o.put("actions", a);
		return o;
	}
	
	@Override
	public Set<WebinterfaceJSModule> getRequiredModules() {
		return Collections.singleton(DefaultJSModule.BASE_ACTIONS);
	}
	
}
