package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.util.Arrays;
import java.util.List;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;

public class MultiAction implements WebinterfaceAction {

	private List<WebinterfaceAction> actions;
	
	public MultiAction(List<WebinterfaceAction> actions) {
		this.actions = actions;
	}
	
	public MultiAction(WebinterfaceAction... actions) {
		this(Arrays.asList(actions));
	}
	
	@Override
	public JavaScriptFunction toJavaScript() {
		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
		StringBuilder code = new StringBuilder();
		for(WebinterfaceAction ac : actions) {
			JavaScriptFunction fc = ac.toJavaScript();
			code.append(fc.getCode().get());
		}
		f.setCode(code.toString());
		return f;
	}
	
}
