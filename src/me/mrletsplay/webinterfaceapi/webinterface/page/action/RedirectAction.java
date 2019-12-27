package me.mrletsplay.webinterfaceapi.webinterface.page.action;
import me.mrletsplay.webinterfaceapi.js.JavaScriptFunction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;

public class RedirectAction implements WebinterfaceAction {

	private WebinterfaceActionValue url;
	
	public RedirectAction(WebinterfaceActionValue url) {
		this.url = url;
	}
	
	public RedirectAction(String url) {
		this(new StringValue(url));
	}
	
	@Override
	public JavaScriptFunction toJavaScript() {
		JavaScriptFunction f = new JavaScriptFunction(randomFunctionName() + "()");
		f.setCode("window.location.href=" + url.toJavaScript());
		return f;
	}

}
