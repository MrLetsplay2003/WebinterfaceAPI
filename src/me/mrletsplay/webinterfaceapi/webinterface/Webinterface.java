package me.mrletsplay.webinterfaceapi.webinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.main.MrCoreServiceRegistry;
import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.HttpStatusCodes;
import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthException;
import me.mrletsplay.webinterfaceapi.webinterface.auth.FileAccountStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountData;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.DiscordAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.GitHubAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.GoogleAuth;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.ElementLayout;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.session.FileSessionStorage;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSessionStorage;

public class Webinterface {
	
	private static HttpServer server = new HttpServer(8080);
	private static List<WebinterfacePage> pages;
	private static List<WebinterfaceActionHandler> handlers;
	private static List<WebinterfaceAuthMethod> authMethods;
	private static WebinterfaceAccountStorage accountStorage;
	private static WebinterfaceSessionStorage sessionStorage;
	
	static {
		pages = new ArrayList<>();
		handlers = new ArrayList<>();
		authMethods = new ArrayList<>();
		accountStorage = new FileAccountStorage(new File("data/accounts.yml"));
		sessionStorage = new FileSessionStorage(new File("data/sessions.yml"));
		
		server.getDocumentProvider().registerDocument("/_internal/include.js", new FileDocument(new File("include/script.js"), "application/javascript"));
		server.getDocumentProvider().registerDocument("/_internal/include.css", new FileDocument(new File("include/style.css"), "text/css"));
		server.getDocumentProvider().registerDocument("/_internal/list.png", new FileDocument(new File("include/list.png"), "image/png"));
		server.getDocumentProvider().registerDocument("/_internal/close.png", new FileDocument(new File("include/close.png"), "image/png"));
		server.getDocumentProvider().registerDocument("/_internal/header.png", new FileDocument(new File("include/webinterfaceapi.png"), "image/png"));
		server.getDocumentProvider().registerDocument("/favicon.ico", new FileDocument(new File("include/favicon.ico")));
		server.getDocumentProvider().registerDocument("/_internal/call", new WebinterfaceCallbackDocument());
		server.getDocumentProvider().registerDocument("/login", new WebinterfaceLoginDocument());
		server.getDocumentProvider().registerDocument("/logout", new WebinterfaceLogoutDocument());
		
		registerAuthMethod(new DiscordAuth());
		registerAuthMethod(new GoogleAuth());
		registerAuthMethod(new GitHubAuth());
		
		WebinterfacePage homePage = new WebinterfacePage("Home", "/");
		WebinterfacePageSection sc = new WebinterfacePageSection();
		WebinterfaceTitleText tt = new WebinterfaceTitleText(() -> "Welcome to WebinterfaceAPI, " + WebinterfaceSession.getCurrentSession().getAccount().getName());
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
	
	public static List<WebinterfacePage> getPages() {
		return pages;
	}
	
	public static void registerActionHandler(WebinterfaceActionHandler handler) {
		handlers.add(handler);
	}
	
	public static List<WebinterfaceActionHandler> getActionHandlers() {
		return handlers;
	}
	
	public static void registerAuthMethod(WebinterfaceAuthMethod method) {
		authMethods.add(method);
		server.getDocumentProvider().registerDocument("/auth/" + method.getID(), method::handleAuthRequest);
		server.getDocumentProvider().registerDocument("/auth/" + method.getID() + "/response", () -> {
			HttpRequestContext c = HttpRequestContext.getCurrentContext();
			try {
				WebinterfaceAccountData acc = method.handleAuthResponse();
				WebinterfaceSession.stopSession();
				WebinterfaceSession s = WebinterfaceSession.startSession(acc);
				c.getServerHeader().getFields().setCookie(WebinterfaceSession.COOKIE_NAME, s.getSessionID(), "Path=/", "Expires=" + WebinterfaceUtils.httpTimeStamp(s.getExpiresAt()));
				c.getServerHeader().setStatusCode(HttpStatusCodes.SEE_OTHER_303);
				c.getServerHeader().getFields().setFieldValue("Location", "/");
			} catch(AuthException e) {
				c.getServerHeader().setContent("text/plain", "Auth failed".getBytes()); // TODO: handle exc msg
			}catch(Exception e) {
				c.getServerHeader().setContent("text/plain", "Auth failed".getBytes()); // TODO: handle exc msg
			}
		});
	}
	
	public static List<WebinterfaceAuthMethod> getAuthMethods() {
		return authMethods;
	}
	
	public static void setAccountStorage(WebinterfaceAccountStorage accountStorage) {
		Webinterface.accountStorage = accountStorage;
	}
	
	public static WebinterfaceAccountStorage getAccountStorage() {
		return accountStorage;
	}
	
	public static void setSessionStorage(WebinterfaceSessionStorage sessionStorage) {
		Webinterface.sessionStorage = sessionStorage;
	}
	
	public static WebinterfaceSessionStorage getSessionStorage() {
		return sessionStorage;
	}
	
	public static void shutdown() {
		server.shutdown();
	}

}
