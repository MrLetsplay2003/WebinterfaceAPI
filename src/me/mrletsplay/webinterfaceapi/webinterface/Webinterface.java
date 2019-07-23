package me.mrletsplay.webinterfaceapi.webinterface;

import java.io.File;

import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementGroup;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementLayout;

public class Webinterface {
	
	private static HttpServer server = new HttpServer(8080);
	
	static {
		server.getDocumentProvider().registerDocument("/_internal/include.js", new FileDocument(new File("TEST/script.js"), "application/javascript"));
		server.getDocumentProvider().registerDocument("/_internal/include.css", new FileDocument(new File("TEST/style.css"), "text/css"));
		
		WebinterfacePage p = new WebinterfacePage("Test page");
		WebinterfacePageSection s = new WebinterfacePageSection();
		
		WebinterfaceElementGroup g = new WebinterfaceElementGroup();
		g.addElement(new WebinterfaceButton("Button #1"));
		
		WebinterfaceButton b = new WebinterfaceButton("Button #2");
		b.setLayout(WebinterfaceElementLayout.FULL_WIDTH);
		g.addElement(b);
		
		WebinterfaceButton b2 = new WebinterfaceButton("Save");
		b2.setWidth("300px");
		b2.setLayout(WebinterfaceElementLayout.FULL_WIDTH);
		g.addElement(b2);

		s.addElement(g);
		s.addElement(g);
		s.addElement(g);
		s.addElement(g);
		
		p.addSection(s);
		p.addSection(s);
		server.getDocumentProvider().registerDocument("/", p);
	}
	
	public static void start() {
		server.start();
	}
	
	public static HttpServer getServer() {
		return server;
	}
	
	public static void shutdown() {
		server.shutdown();
	}

}
