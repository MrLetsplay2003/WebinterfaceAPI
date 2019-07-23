package me.mrletsplay.webinterfaceapi.webinterface;

import java.io.File;

import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceButton;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementLayout;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageSection;

public class Webinterface {
	
	private static HttpServer server = new HttpServer(8080);
	
	static {
		server.getDocumentProvider().registerDocument("/_internal/include.js", new FileDocument(new File("TEST/script.js"), "application/javascript"));
		server.getDocumentProvider().registerDocument("/_internal/include.css", new FileDocument(new File("TEST/style.css"), "text/css"));
		
		WebinterfacePage p = new WebinterfacePage();
		WebinterfacePageSection s = new WebinterfacePageSection();
		s.setLayout(WebinterfaceElementLayout.FULL_WIDTH_BOX);
		s.addElement(new WebinterfaceButton());
		p.addSection(s);
		p.addSection(s);
		p.addSection(s);
		p.addSection(s);
		p.addSection(s);
		p.addSection(s);
		p.addSection(s);
		p.addSection(s);
		server.getDocumentProvider().registerDocument("/a", p.toHtml());
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
