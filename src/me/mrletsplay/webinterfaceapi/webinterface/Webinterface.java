package me.mrletsplay.webinterfaceapi.webinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.main.MrCoreServiceRegistry;
import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;

public class Webinterface {
	
	private static HttpServer server = new HttpServer(8080);
	private static List<WebinterfacePage> pages;
	private static List<WebinterfaceActionHandler> handlers;
	
	static {
		server.getDocumentProvider().registerDocument("/_internal/include.js", new FileDocument(new File("include/script.js"), "application/javascript"));
		server.getDocumentProvider().registerDocument("/_internal/include.css", new FileDocument(new File("include/style.css"), "text/css"));
		server.getDocumentProvider().registerDocument("/_internal/header.png", new FileDocument(new File("include/webinterfaceapi.png"), "image/png"));
		server.getDocumentProvider().registerDocument("/favicon.ico", new FileDocument(new File("include/favicon.ico")));
		server.getDocumentProvider().registerDocument("/_internal/call", new WebinterfaceCallbackDocument());
		
		pages = new ArrayList<>();
		handlers = new ArrayList<>();
		MrCoreServiceRegistry.registerService("WebinterfaceAPI", null);
	}
	
	public static void start() {
		server.start();
	}
	
	public static HttpServer getServer() {
		return server;
	}
	
	public static void registerPage(WebinterfacePage page) {
		pages.add(page);
		server.getDocumentProvider().registerDocument(page.getUrl(), page);
	}
	
	public static List<WebinterfacePage> getPages() {
		return pages;
	}
	
	public static void registerActionHandler(WebinterfaceActionHandler handler) {
		handlers.add(handler);
	}
	
	public static List<WebinterfaceActionHandler> getActionHandlers() {
		return handlers;
	}
	
	public static void shutdown() {
		server.shutdown();
	}

}
