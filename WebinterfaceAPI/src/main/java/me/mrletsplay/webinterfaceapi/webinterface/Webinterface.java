package me.mrletsplay.webinterfaceapi.webinterface;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.main.MrCoreServiceRegistry;
import me.mrletsplay.mrcore.misc.ByteUtils;
import me.mrletsplay.simplehttpserver.http.HttpServer;
import me.mrletsplay.simplehttpserver.http.HttpsServer;
import me.mrletsplay.simplehttpserver.http.document.HttpDocumentProvider;
import me.mrletsplay.simplehttpserver.http.websocket.WebSocketEndpoint;
import me.mrletsplay.simplehttpserver.php.PHP;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AccountStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.auth.CredentialsStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.FileAccountStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.FileCredentialsStorage;
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
import me.mrletsplay.webinterfaceapi.webinterface.document.DocumentProvider;
import me.mrletsplay.webinterfaceapi.webinterface.document.FileUploadDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.HomeDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.LoginDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.LogoutDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.PasswordLoginDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.SetupDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.SetupSubmitDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.websocket.Packet;
import me.mrletsplay.webinterfaceapi.webinterface.document.websocket.WebSocketData;
import me.mrletsplay.webinterfaceapi.webinterface.document.websocket.WebSocketDocument;
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
	private static List<AuthMethod> authMethods;
	private static List<JSModule> jsModules;
	private static Map<String, File> includedFiles;

	private static boolean initialized = false;
	private static File rootDirectory;
	private static AccountStorage accountStorage;
	private static SessionStorage sessionStorage;
	private static CredentialsStorage credentialsStorage;
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

		if(documentProvider == null) documentProvider = new DocumentProvider();

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
		documentProvider.registerDocument("/_internal/fileupload", new FileUploadDocument());
		webSocketEndpoint = new WebSocketDocument();
		documentProvider.registerDocument("/_internal/ws", webSocketEndpoint);
		documentProvider.registerDocument("/", new HomeDocument());
		documentProvider.registerDocument("/login", new LoginDocument());
		documentProvider.registerDocument("/logout", new LogoutDocument());
		if(config.getSetting(DefaultSettings.ENABLE_PASSWORD_AUTH)) documentProvider.registerDocument("/auth/password/login", new PasswordLoginDocument());

		pages.forEach(page -> documentProvider.registerDocument(page.getUrl(), page));
		categories.forEach(category -> category.getPages().forEach(page -> documentProvider.registerDocument(page.getUrl(), page)));

		authMethods.forEach(Webinterface::registerAuthPages);
		jsModules.forEach(Webinterface::registerJSModulePage);

		Integer setupStep = config.getOverride(SetupDocument.SETUP_STEP_OVERRIDE_PATH, Integer.class);
		if(config.getSetting(DefaultSettings.ENABLE_INITIAL_SETUP) && (setupStep == null || setupStep < SetupDocument.SETUP_STEP_DONE)) {
			documentProvider.registerDocument("/setup", new SetupDocument());
			documentProvider.registerDocument("/setup/submit", new SetupSubmitDocument());
		}

		initialized = true;
		service.fire(WebinterfaceService.EVENT_POST_INIT);
	}

	public static void start() {
		service.fire(WebinterfaceService.EVENT_PRE_START);

		initialize();
		extractResources("/webinterfaceapi-resources.list");

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

	/**
	 * Extracts resources from the JAR file.<br>
	 * Resources must be specified in a resource index file as generated by the <a href="https://github.com/MrLetsplay2003/resource-index-maven-plugin">resource-index-maven-plugin</a>
	 * @param resourceIndexPath The path to the resource index in the JAR file, retrieved using {@link Class#getResource(String) Webinterface.class.getResource(String)}
	 */
	public static void extractResources(String resourceIndexPath) {
		try {
			Webinterface.getLogger().info("Extracting resources from index '" + resourceIndexPath + "'");
			URL url = Webinterface.class.getResource(resourceIndexPath);
			if(url == null) {
				Webinterface.getLogger().warn("Couldn't find resource index '" + resourceIndexPath + "'. Not extracting resources!");
				return;
			}

			Path resourcesPath = getRootDirectory().toPath().resolve("data/resources.json");
			JSONObject resourcesObj;
			if(Files.exists(resourcesPath)) {
				resourcesObj = new JSONObject(Files.readString(resourcesPath, StandardCharsets.UTF_8));
			}else {
				resourcesObj = new JSONObject();
				resourcesObj.put("_comment", "Do not edit this file. It is used to check whether resource files have been modified, so they can be updated automatically.");
			}

			MessageDigest md = MessageDigest.getInstance("MD5");
			String[] resources = new String(IOUtils.readAllBytes(url.openStream()), StandardCharsets.UTF_8).split("\n");
			for(String resource : resources) {
				URL resURL = Webinterface.class.getResource("/" + resource);
				if(resURL == null) {
					Webinterface.getLogger().warn("Couldn't find resource '" + resource + "' in JAR file");
					continue;
				}

				String expectedDigest = resourcesObj.optString(resource).orElse(null);
				byte[] resBytes = IOUtils.readAllBytes(resURL.openStream());
				byte[] digest = md.digest(resBytes);
				String digestStr = ByteUtils.bytesToHex(digest);

				Path outFile = getRootDirectory().toPath().resolve(resource);
				if(Files.exists(outFile)) {
					if(expectedDigest == null) {
						Webinterface.getLogger().warn("Resource file '" + resource + "' already exists, but there is no digest entry in the resources.json. Skipping!");
						continue;
					}

					if(expectedDigest.equals(digestStr)) continue; // File isn't supposed to change, skip

					byte[] bytes = Files.readAllBytes(outFile);
					byte[] resDigest = md.digest(bytes);
					if(!ByteUtils.bytesToHex(resDigest).equals(expectedDigest)) {
						// TODO: Add command line option to force override file
						Webinterface.getLogger().warn("Resource file '" + resource + "' has been edited. To force updating this file, add the '--force-update-resources' option. Skipping!");
						continue;
					}

					// File hasn't been edited since the last update, automatically update it
				}

				Webinterface.getLogger().info("Extracting resource '" + resource + "'...");
				IOUtils.createFile(outFile.toFile());
				Files.write(outFile, resBytes);
				resourcesObj.put(resource, digestStr);
			}

			Files.createDirectories(resourcesPath.getParent());
			Files.writeString(resourcesPath, resourcesObj.toFancyString(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			Webinterface.getLogger().error("Error while extracting resources from index '" + resourceIndexPath + "'", e);
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

	public static void registerAuthMethod(AuthMethod method) {
		if(authMethods.stream().anyMatch(a -> a.getID().equals(method.getID()))) return;
		authMethods.add(method);
		if(initialized) registerAuthPages(method);
	}

	private static void registerAuthPages(AuthMethod method) {
		documentProvider.registerDocument("/auth/" + method.getID(), new AuthRequestDocument(method));
		documentProvider.registerDocument("/auth/" + method.getID() + "/response", new AuthResponseDocument(method));
	}

	public static List<AuthMethod> getAuthMethods() {
		return authMethods;
	}

	public static AuthMethod getAuthMethodByID(String id) {
		return authMethods.stream()
				.filter(a -> a.getID().equals(id))
				.findFirst().orElse(null);
	}

	public static List<AuthMethod> getAvailableAuthMethods() {
		return authMethods.stream().filter(AuthMethod::isAvailable).collect(Collectors.toList());
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

	public static void setAccountStorage(AccountStorage accountStorage) {
		Webinterface.accountStorage = accountStorage;
	}

	public static AccountStorage getAccountStorage() {
		return accountStorage;
	}

	public static void setSessionStorage(SessionStorage sessionStorage) {
		Webinterface.sessionStorage = sessionStorage;
	}

	public static SessionStorage getSessionStorage() {
		return sessionStorage;
	}

	public static void setCredentialsStorage(CredentialsStorage credentialsStorage) {
		Webinterface.credentialsStorage = credentialsStorage;
	}

	public static CredentialsStorage getCredentialsStorage() {
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
