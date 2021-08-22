package me.mrletsplay.webinterfaceapi.http.document;

import me.mrletsplay.webinterfaceapi.html.HtmlDocument;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.header.HttpServerHeader;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;

public class Default500Document implements HttpDocument {
	
	private HtmlDocument doc;
	
	public Default500Document() {
		this.doc = new HtmlDocument();
		doc.setTitle("500 Internal Server Error");
		doc.setDescription("500 Page");
		
		HtmlElement h1 = new HtmlElement("h1");
		h1.setText("500 Internal Server Error");
		doc.getBodyNode().appendChild(h1);
		doc.getBodyNode().appendChild(HtmlElement.br());
		
		HtmlElement i = new HtmlElement("i");
		i.setText("An unexpected error occurred while processing your request");
		doc.getBodyNode().appendChild(i);
		doc.getBodyNode().appendChild(HtmlElement.br());
		
		HtmlElement add = new HtmlElement("pre");
		add.setText(() -> {
			if(!Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_DEBUG_MODE)) return "Enable debug mode to see more information about this error";
			StringBuilder b = new StringBuilder("Stack trace:\n");
			Exception e = HttpRequestContext.getCurrentContext().getException();
			append(b, e, false);
			return b.toString();
		});
		doc.getBodyNode().appendChild(add);
		
		HtmlElement p = HtmlElement.p();
		p.setText("WebinterfaceAPI (Java) Http Server");
		p.setAttribute("style", "font-size: 12px");
		doc.getBodyNode().appendChild(p);
	}
	
	private void append(StringBuilder b, Throwable t, boolean isCause) {
		if(isCause) b.append("\nCaused by: ");
		b.append(t.toString());
		for(StackTraceElement el : t.getStackTrace()) {
			b.append("\n\t").append(el.toString());
		}
		if(t.getCause() != null) append(b, t, true);
	}
	
	@Override
	public void createContent() {
		HttpServerHeader h = HttpRequestContext.getCurrentContext().getServerHeader();
		doc.createContent();
		h.setStatusCode(HttpStatusCodes.INTERNAL_SERVER_ERROR_500);
	}

}
