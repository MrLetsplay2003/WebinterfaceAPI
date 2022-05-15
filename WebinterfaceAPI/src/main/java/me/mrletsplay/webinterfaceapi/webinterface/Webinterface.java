package me.mrletsplay.webinterfaceapi.webinterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.main.MrCoreServiceRegistry;
import me.mrletsplay.simplehttpserver.http.HttpServer;
import me.mrletsplay.simplehttpserver.http.HttpsServer;
import me.mrletsplay.simplehttpserver.http.document.HttpDocumentProvider;
import me.mrletsplay.simplehttpserver.http.websocket.WebSocketEndpoint;
import me.mrletsplay.simplehttpserver.php.PHP;
import me.mrletsplay.webinterfaceapi.webinterface.auth.FileAccountStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.FileCredentialsStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceCredentialsStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.DiscordAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.GitHubAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.GoogleAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.NoAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.PasswordAuth;
import me.mrletsplay.webinterfaceapi.webinterface.config.Config;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.webinterface.config.FileConfig;
import me.mrletsplay.webinterfaceapi.webinterface.document.ActionCallbackDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.AuthRequestDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.AuthResponseDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceDocumentProvider;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceFileUploadDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceHomeDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceLoginDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceLogoutDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfacePasswordLoginDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceSetupDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceSetupSubmitDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.websocket.Packet;
import me.mrletsplay.webinterfaceapi.webinterface.document.websocket.WebSocketData;
import me.mrletsplay.webinterfaceapi.webinterface.document.websocket.WebinterfaceWebSocketDocument;
import me.mrletsplay.webinterfaceapi.webinterface.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.webinterface.js.JSModule;
import me.mrletsplay.webinterfaceapi.webinterface.markdown.MarkdownRenderer;
import me.mrletsplay.webinterfaceapi.webinterface.page.Page;
import me.mrletsplay.webinterfaceapi.webinterface.page.PageCategory;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.event.WebinterfaceEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.AccountPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.AccountsPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.DefaultSettingsPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.PermissionsPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.WelcomePage;
import me.mrletsplay.webinterfaceapi.webinterface.session.FileSessionStorage;
import me.mrletsplay.webinterfaceapi.webinterface.session.Session;
import me.mrletsplay.webinterfaceapi.webinterface.session.SessionStorage;

public class Webinterface {

	private static Logger logger;

	private static WebinterfaceService service;

	private static HttpDocumentProvider documentProvider;

	private static HttpServer httpServer;
	private static HttpsServer httpsServer;

	private static List<Page> pages;
	private static List<PageCategory> categories;
	private static List<ActionHandler> handlers;
	private static List<WebinterfaceEvent> events;
	private static List<WebinterfaceAuthMethod> authMethods;
	private static List<JSModule> jsModules;
	private static Map<String, File> includedFiles;

	private static boolean initialized = false;
	private static File rootDirectory;
	private static WebinterfaceAccountStorage accountStorage;
	private static SessionStorage sessionStorage;
	private static WebinterfaceCredentialsStorage credentialsStorage;
	private static Config config;
	private static MarkdownRenderer markdownRenderer;
	private static WebSocketEndpoint webSocketEndpoint;

	static {
		pages = new ArrayList<>();
		categories = new ArrayList<>();
		handlers = new ArrayList<>();
		events = new ArrayList<>();
		authMethods = new ArrayList<>();
		jsModules = new ArrayList<>();
		includedFiles = new HashMap<>();
		rootDirectory = new File(Paths.get("").toAbsolutePath().toString());
		markdownRenderer = new MarkdownRenderer();

		PageCategory cat = createCategory("WebinterfaceAPI");
		cat.addPage(new WelcomePage());
		cat.addPage(new AccountsPage());
		cat.addPage(new PermissionsPage());
		cat.addPage(new AccountPage());
		cat.addPage(new DefaultSettingsPage());

		registerActionHandler(new DefaultHandler());

		service = new WebinterfaceService();
		MrCoreServiceRegistry.registerService("WebinterfaceAPI", service);
	}

	private static void initialize() {
		service.fire(WebinterfaceService.EVENT_PRE_INIT);

		if(System.getProperty("webinterface.log-dir") == null)
			System.setProperty("webinterface.log-dir", new File(rootDirectory, "logs").getAbsolutePath());
		logger = LoggerFactory.getLogger(Webinterface.class);

		includeFile("/_internal", new File(rootDirectory, "include"));
		accountStorage = new FileAccountStorage(new File(rootDirectory, "data/accounts.yml"));
		sessionStorage = new FileSessionStorage(new File(rootDirectory, "data/sessions.yml"));
		credentialsStorage = new FileCredentialsStorage(new File(rootDirectory, "data/credentials.yml"));

		config = new FileConfig(new File(getConfigurationDirectory(), "config.yml"));
		config.registerSettings(DefaultSettings.INSTANCE);

		registerAuthMethod(new NoAuth());
		registerAuthMethod(new DiscordAuth());
		registerAuthMethod(new GoogleAuth());
		registerAuthMethod(new GitHubAuth());
		registerAuthMethod(new PasswordAuth());

		Arrays.stream(DefaultJSModule.values()).forEach(Webinterface::registerJSModule);

		PHP.setEnabled(config.getSetting(DefaultSettings.ENABLE_PHP));
		PHP.setCGIPath(config.getSetting(DefaultSettings.PHP_CGI_PATH));
		PHP.setFileExtensions(config.getSetting(DefaultSettings.PHP_FILE_EXTENSIONS));

		if(documentProvider == null) documentProvider = new WebinterfaceDocumentProvider();

		httpServer = new HttpServer(HttpServer.newConfigurationBuilder()
				.host(config.getSetting(DefaultSettings.HTTP_BIND))
				.port(config.getSetting(DefaultSettings.HTTP_PORT))
				.debugMode(config.getSetting(DefaultSettings.ENABLE_DEBUG_MODE))
				.create());
		httpServer.setDocumentProvider(documentProvider);

		if(config.getSetting(DefaultSettings.HTTPS_ENABLE)) {
			String certPath = config.getSetting(DefaultSettings.HTTPS_CERTIFICATE_PATH);
			String certKeyPath = config.getSetting(DefaultSettings.HTTPS_CERTIFICATE_KEY_PATH);

			if(certPath == null || certKeyPath == null) {
				logger.warn("Both certificate and certificate key need to be configured to use HTTPS");
			}else {
				File certFile = new File(certPath);
				File certKeyFile = new File(certKeyPath);
				String password = config.getSetting(DefaultSettings.HTTPS_CERTIFICATE_PASSWORD);
				httpsServer = new HttpsServer(HttpsServer.newConfigurationBuilder()
						.host(config.getSetting(DefaultSettings.HTTPS_BIND))
						.port(config.getSetting(DefaultSettings.HTTPS_PORT))
						.debugMode(config.getSetting(DefaultSettings.ENABLE_DEBUG_MODE))
						.certificate(certFile, certKeyFile)
						.certificatePassword(password)
						.create());
				httpsServer.setDocumentProvider(documentProvider);
			}
		}

		loadIncludedFiles();

		documentProvider.registerDocument("/_internal/call", new ActionCallbackDocument());
		documentProvider.registerDocument("/_internal/fileupload", new WebinterfaceFileUploadDocument());
		webSocketEndpoint = new WebinterfaceWebSocketDocument();
		documentProvider.registerDocument("/_internal/ws", webSocketEndpoint);
		documentProvider.registerDocument("/", new WebinterfaceHomeDocument());
		documentProvider.registerDocument("/login", new WebinterfaceLoginDocument());
		documentProvider.registerDocument("/logout", new WebinterfaceLogoutDocument());
		if(config.getSetting(DefaultSettings.ENABLE_PASSWORD_AUTH)) documentProvider.registerDocument("/auth/password/login", new WebinterfacePasswordLoginDocument());

		pages.forEach(page -> documentProvider.registerDocument(page.getUrl(), page));
		categories.forEach(category -> category.getPages().forEach(page -> documentProvider.registerDocument(page.getUrl(), page)));

		authMethods.forEach(Webinterface::registerAuthPages);
		jsModules.forEach(Webinterface::registerJSModulePage);

		Integer setupStep = config.getOverride(WebinterfaceSetupDocument.SETUP_STEP_OVERRIDE_PATH, Integer.class);
		if(config.getSetting(DefaultSettings.ENABLE_INITIAL_SETUP) && (setupStep == null || setupStep < WebinterfaceSetupDocument.SETUP_STEP_DONE)) {
			documentProvider.registerDocument("/setup", new WebinterfaceSetupDocument());
			documentProvider.registerDocument("/setup/submit", new WebinterfaceSetupSubmitDocument());
		}

		initialized = true;
		service.fire(WebinterfaceService.EVENT_POST_INIT);
	}

	public static void start() {
		service.fire(WebinterfaceService.EVENT_PRE_START);

		initialize();
		extractFiles();

		accountStorage.initialize();
		sessionStorage.initialize();
		credentialsStorage.initialize();

		try {
			httpServer.start();
			if(httpsServer != null) httpsServer.start();
		}catch(Exception e) {
			logger.error("Failed to start http(s) server", e);
			return;
		}

		httpServer.getExecutor().execute(() -> {
			Supplier<Boolean> keepRunning = () -> httpServer.isRunning() && !httpServer.getExecutor().isShutdown() && !Thread.interrupted();
			while(keepRunning.get()) {
				for(Session sess : getSessionStorage().getSessions()) {
					if(sess.hasExpired()) getSessionStorage().deleteSession(sess.getSessionID());
				}
				for(int i = 0; i < 10; i++) {
					if(!keepRunning.get()) return;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignored) {}
				}
			}
		});

		service.fire(WebinterfaceService.EVENT_POST_START);
	}

	private static void extractFiles() {
		try {
			URL jarLoc = Webinterface.class.getProtectionDomain().getCodeSource().getLocation();
			File jarFl = new File(jarLoc.toURI().getPath());
			if(!jarFl.isFile()) return;
			Webinterface.getLogger().info("Extracting files from \"" + jarFl.getAbsolutePath() + "\"...");
			try (JarFile fl = new JarFile(jarFl)) {
				Enumeration<JarEntry> en = fl.entries();
				while(en.hasMoreElements()) {
					JarEntry e = en.nextElement();
					if(!e.isDirectory() && e.getName().startsWith("include/")) {
						File ofl = new File(getRootDirectory(), e.getName());
						if(ofl.exists()) continue;
						Webinterface.getLogger().debug("Extracting " + e.getName() + "...");
						IOUtils.createFile(ofl);
						try (InputStream in = fl.getInputStream(e);
								OutputStream out = new FileOutputStream(ofl)) {
							IOUtils.transfer(in, out);
						}
					}
				}
			}catch(IOException e) {
				Webinterface.getLogger().error("Error while extracting required files", e);
			}
		} catch (Exception e) {
			Webinterface.getLogger().error("Error while locating required files", e);
		}
	}

	public static boolean isInitialized() {
		return initialized;
	}

	public static HttpServer getHttpServer() {
		return httpServer;
	}

	public static HttpsServer getHttpsServer() {
		return httpsServer;
	}

	public static void setDocumentProvider(HttpDocumentProvider documentProvider) {
		Webinterface.documentProvider = documentProvider;
	}

	public static HttpDocumentProvider getDocumentProvider() {
		return documentProvider;
	}

	public static void setRootDirectory(File rootDirectory) {
		Webinterface.rootDirectory = rootDirectory;
	}

	public static File getRootDirectory() {
		return rootDirectory;
	}

	public static File getConfigurationDirectory() {
		return new File(rootDirectory, "cfg/");
	}

	public static Config getConfig() {
		return config;
	}

	public static void registerPage(Page page) {
		pages.add(page);
		if(initialized) documentProvider.registerDocument(page.getUrl(), page);
	}

	public static List<Page> getPages() {
		return pages;
	}

	public static void registerCategory(PageCategory category) {
		categories.add(category);
	}

	public static PageCategory createCategory(String name) {
		PageCategory cat = new PageCategory(name);
		registerCategory(cat);
		return cat;
	}

	public static List<PageCategory> getCategories() {
		return categories;
	}

	public static void registerActionHandler(ActionHandler handler) {
		handlers.add(handler);
	}

	public static List<ActionHandler> getActionHandlers() {
		return handlers;
	}

	public static WebinterfaceEvent registerEvent(String target, String eventName, String permission) {
		if(getEvent(target, eventName) != null) throw new IllegalArgumentException("An event with that target and name is already registered");
		WebinterfaceEvent event = new WebinterfaceEvent(target, eventName, permission);
		events.add(event);
		return event;
	}

	public static WebinterfaceEvent registerEvent(String target, String eventName) {
		return registerEvent(target, eventName, null);
	}

	public static void unregisterEvent(WebinterfaceEvent event) {
		events.remove(event);
		webSocketEndpoint.getConnections().forEach(c -> {
			WebSocketData d = c.getAttachment();
			if(d != null) d.unsubscribe(event);
		});
	}

	public static WebinterfaceEvent getEvent(String target, String eventName) {
		return events.stream()
				.filter(e -> e.getTarget().equals(target) && e.getEventName().equals(eventName))
				.findFirst().orElse(null);
	}

	public static List<WebinterfaceEvent> getEvents() {
		return events;
	}

	public static void broadcastEvent(WebinterfaceEvent event, JSONObject eventData) {
		if(!events.contains(event)) throw new IllegalArgumentException("Trying to throw an unregistered event");
		webSocketEndpoint.getConnections().forEach(ws -> {
			WebSocketData d = ws.getAttachment();
			if(d != null && d.getSubscribedEvents().contains(event)) {
				ws.sendText(Packet.of(event.getTarget(), event.getEventName(), eventData).toJSON().toString());
			}
		});
	}

	public static void registerAuthMethod(WebinterfaceAuthMethod method) {
		if(authMethods.stream().anyMatch(a -> a.getID().equals(method.getID()))) return;
		authMethods.add(method);
		if(initialized) registerAuthPages(method);
	}

	private static void registerAuthPages(WebinterfaceAuthMethod method) {
		documentProvider.registerDocument("/auth/" + method.getID(), new AuthRequestDocument(method));
		documentProvider.registerDocument("/auth/" + method.getID() + "/response", new AuthResponseDocument(method));
	}

	public static List<WebinterfaceAuthMethod> getAuthMethods() {
		return authMethods;
	}

	public static WebinterfaceAuthMethod getAuthMethodByID(String id) {
		return authMethods.stream()
				.filter(a -> a.getID().equals(id))
				.findFirst().orElse(null);
	}

	public static List<WebinterfaceAuthMethod> getAvailableAuthMethods() {
		return authMethods.stream().filter(WebinterfaceAuthMethod::isAvailable).collect(Collectors.toList());
	}

	public static void registerJSModule(JSModule module) {
		jsModules.add(module);
		if(initialized) registerJSModulePage(module);
	}

	private static void registerJSModulePage(JSModule module) {
		documentProvider.registerDocument("/_internal/module/" + module.getIdentifier() + ".js", module);
	}

	public static List<JSModule> getJSModules() {
		return jsModules;
	}

	public static void setAccountStorage(WebinterfaceAccountStorage accountStorage) {
		Webinterface.accountStorage = accountStorage;
	}

	public static WebinterfaceAccountStorage getAccountStorage() {
		return accountStorage;
	}

	public static void setSessionStorage(SessionStorage sessionStorage) {
		Webinterface.sessionStorage = sessionStorage;
	}

	public static SessionStorage getSessionStorage() {
		return sessionStorage;
	}

	public static void setCredentialsStorage(WebinterfaceCredentialsStorage credentialsStorage) {
		Webinterface.credentialsStorage = credentialsStorage;
	}

	public static WebinterfaceCredentialsStorage getCredentialsStorage() {
		return credentialsStorage;
	}

	public static void setMarkdownRenderer(MarkdownRenderer markdownRenderer) {
		Webinterface.markdownRenderer = markdownRenderer;
	}

	public static MarkdownRenderer getMarkdownRenderer() {
		return markdownRenderer;
	}

	public static void includeFile(String path, File file) {
		includedFiles.put(path, file);
	}

	public static void loadIncludedFiles() {
		for(Map.Entry<String, File> v : includedFiles.entrySet()) {
			documentProvider.registerFileDocument(v.getKey(), v.getValue());
		}
	}

	public static Map<String, File> getIncludedFiles() {
		return includedFiles;
	}

	public static void shutdown() {
		initialized = false;
		if(httpServer != null) httpServer.shutdown();
		if(httpsServer != null) httpsServer.shutdown();
	}

	public static Logger getLogger() {
		return logger;
	}

}