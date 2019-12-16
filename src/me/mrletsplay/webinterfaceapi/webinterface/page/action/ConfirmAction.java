package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;

public class ConfirmAction implements WebinterfaceAction {
	
	private WebinterfaceAction action;
	
	public ConfirmAction(WebinterfaceAction action) {
		this.action = action;
	}
	
	@Override
	public JavaScriptFunction toJavaScript() {
		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
		StringBuilder code = new StringBuilder();
		code.append("if(confirm(\"Are you sure?\")) {");
		JavaScriptFunction fc = action.toJavaScript();
		code.append(fc.getCode().get());
		code.append("}");
		f.setCode(code.toString());
		return f;
	}

}
