package me.mrletsplay.webinterfaceapi.webinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.main.MrCoreServiceRegistry;
import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.DiscordAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.GoogleAuth;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.ElementLayout;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;

public class Webinterface {
	
	private static HttpServer server = new HttpServer(8080);
	private static List<WebinterfacePage> pages;
	private static List<WebinterfaceActionHandler> handlers;
	
	static {
		pages = new ArrayList<>();
		handlers = new ArrayList<>();
		
		server.getDocumentProvider().registerDocument("/_internal/include.js", new FileDocument(new File("include/script.js"), "application/javascript"));
		server.getDocumentProvider().registerDocument("/_internal/include.css", new FileDocument(new File("include/style.css"), "text/css"));
		server.getDocumentProvider().registerDocument("/_internal/list.png", new FileDocument(new File("include/list.png"), "image/png"));
		server.getDocumentProvider().registerDocument("/_internal/close.png", new FileDocument(new File("include/close.png"), "image/png"));
		server.getDocumentProvider().registerDocument("/_internal/header.png", new FileDocument(new File("include/webinterfaceapi.png"), "image/png"));
		server.getDocumentProvider().registerDocument("/favicon.ico", new FileDocument(new File("include/favicon.ico")));
		server.getDocumentProvider().registerDocument("/_internal/call", new WebinterfaceCallbackDocument());
		
		registerAuthMethod(new DiscordAuth());
		registerAuthMethod(new GoogleAuth());
		
		WebinterfacePage homePage = new WebinterfacePage("Home", "/");
		WebinterfacePageSection sc = new WebinterfacePageSection();
		WebinterfaceTitleText tt = new WebinterfaceTitleText("Welcome to WebinterfaceAPI");
		tt.addLayouts(ElementLayout.FULL_WIDTH, ElementLayout.CENTER_VERTICALLY);
		sc.addElement(tt);
		WebinterfaceText tx = new WebinterfaceText("Hello World!");
		tx.addLayouts(ElementLayout.FULL_WIDTH, ElementLayout.CENTER_VERTICALLY);
		sc.addElement(tx);
		homePage.addSection(sc);
		registerPage(homePage);
		
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
	
	public static void registerAuthMethod(WebinterfaceAuthMethod method) {
		server.getDocumentProvider().registerDocument("/auth/" + method.getID(), method::handleAuthRequest);
		server.getDocumentProvider().registerDocument("/auth/" + method.getID() + "/response", () -> {
			HttpRequestContext c = HttpRequestContext.getCurrentContext();
			try {
				method.handleAuthResponse();
				c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
				c.getServerHeader().getFields().setCookie("wi_sessid", "thisisasessionid", "Path=/");
				c.getServerHeader().getFields().setFieldValue("Location", "/");
			} catch(AuthException e) {
				c.getServerHeader().setContent("text/plain", "Auth failed".getBytes()); // TODO: handle exc msg
			}catch(Exception e) {
				c.getServerHeader().setContent("text/plain", "Auth failed".getBytes()); // TODO: handle exc msg
			}
		});
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
