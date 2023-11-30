package me.mrletsplay.webinterfaceapi;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.ByteUtils;
import me.mrletsplay.simplehttpserver.http.HttpRequestMethod;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.cors.CorsConfiguration;
import me.mrletsplay.simplehttpserver.http.document.DefaultDocumentProvider;
import me.mrletsplay.simplehttpserver.http.document.DocumentProvider;
import me.mrletsplay.simplehttpserver.http.header.HttpServerHeader;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.server.HttpServer;
import me.mrletsplay.simplehttpserver.http.server.HttpsServer;
import me.mrletsplay.simplehttpserver.http.websocket.WebSocketEndpoint;
import me.mrletsplay.simplehttpserver.php.PHP;
import me.mrletsplay.webinterfaceapi.auth.AccountStorage;
import me.mrletsplay.webinterfaceapi.auth.AuthMethod;
import me.mrletsplay.webinterfaceapi.auth.CredentialsStorage;
import me.mrletsplay.webinterfaceapi.auth.FileAccountStorage;
import me.mrletsplay.webinterfaceapi.auth.FileCredentialsStorage;
import me.mrletsplay.webinterfaceapi.auth.SQLAccountStorage;
import me.mrletsplay.webinterfaceapi.auth.SQLCredentialsStorage;
import me.mrletsplay.webinterfaceapi.auth.impl.DiscordAuth;
import me.mrletsplay.webinterfaceapi.auth.impl.GitHubAuth;
import me.mrletsplay.webinterfaceapi.auth.impl.GoogleAuth;
import me.mrletsplay.webinterfaceapi.auth.impl.NoAuth;
import me.mrletsplay.webinterfaceapi.auth.impl.PasswordAuth;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.config.FileConfig;
import me.mrletsplay.webinterfaceapi.document.ActionCallbackDocument;
import me.mrletsplay.webinterfaceapi.document.AuthRequestDocument;
import me.mrletsplay.webinterfaceapi.document.AuthResponseDocument;
import me.mrletsplay.webinterfaceapi.document.FileUploadDocument;
import me.mrletsplay.webinterfaceapi.document.HomeDocument;
import me.mrletsplay.webinterfaceapi.document.IncludedFilesDocument;
import me.mrletsplay.webinterfaceapi.document.LoginDocument;
import me.mrletsplay.webinterfaceapi.document.LogoutDocument;
import me.mrletsplay.webinterfaceapi.document.PasswordLoginDocument;
import me.mrletsplay.webinterfaceapi.document.RegistrationSecretDocument;
import me.mrletsplay.webinterfaceapi.document.SetupDocument;
import me.mrletsplay.webinterfaceapi.document.SetupSubmitDocument;
import me.mrletsplay.webinterfaceapi.document.websocket.Packet;
import me.mrletsplay.webinterfaceapi.document.websocket.WebSocketData;
import me.mrletsplay.webinterfaceapi.document.websocket.WebSocketDocument;
import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.js.JSModule;
import me.mrletsplay.webinterfaceapi.markdown.MarkdownRenderer;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageCategory;
import me.mrletsplay.webinterfaceapi.page.action.ActionHandler;
import me.mrletsplay.webinterfaceapi.page.event.WebinterfaceEvent;
import me.mrletsplay.webinterfaceapi.page.impl.AccountPage;
import me.mrletsplay.webinterfaceapi.page.impl.AccountsPage;
import me.mrletsplay.webinterfaceapi.page.impl.DefaultSettingsPage;
import me.mrletsplay.webinterfaceapi.page.impl.PermissionsPage;
import me.mrletsplay.webinterfaceapi.page.impl.WelcomePage;
import me.mrletsplay.webinterfaceapi.session.FileSessionStorage;
import me.mrletsplay.webinterfaceapi.session.SQLSessionStorage;
import me.mrletsplay.webinterfaceapi.session.Session;
import me.mrletsplay.webinterfaceapi.session.SessionStorage;
import me.mrletsplay.webinterfaceapi.setup.Setup;
import me.mrletsplay.webinterfaceapi.setup.impl.DatabaseSetupStep;
import me.mrletsplay.webinterfaceapi.sql.SQLHelper;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceState;

public class Webinterface {

	private static Logger logger;

	private static DocumentProvider documentProvider;

	private static HttpServer httpServer;
	private static HttpsServer httpsServer;

	private static ScheduledExecutorService taskScheduler;

	private static List<Page> pages;
	private static List<PageCategory> categories;
	private static List<ActionHandler> handlers;
	private static List<WebinterfaceEvent> events;
	private static List<AuthMethod> authMethods;
	private static List<JSModule> jsModules;

	private static WebinterfaceState state;

	private static File rootDirectory;
	private static Path includePath;
	private static AccountStorage accountStorage;
	private static SessionStorage sessionStorage;
	private static CredentialsStorage credentialsStorage;
	private static Config config;
	private static MarkdownRenderer markdownRenderer;
	private static WebSocketEndpoint webSocketEndpoint;
	private static Setup setup;

	static {
		cleanUp();
	}

	/**
	 * Cleans up all of the static variables to ensure consistent state between restarts
	 */
	private static void cleanUp() {
		if(System.getProperty("webinterface.log-dir") == null)
			System.setProperty("webinterface.log-dir", new File(rootDirectory, "logs").getAbsolutePath());

		logger = LoggerFactory.getLogger(Webinterface.class);

		documentProvider = new DefaultDocumentProvider();
		httpServer = null;
		httpsServer = null;

		taskScheduler = Executors.newScheduledThreadPool(1);

		pages = new ArrayList<>();
		categories = new ArrayList<>();
		handlers = new ArrayList<>();
		events = new ArrayList<>();
		authMethods = new ArrayList<>();
		jsModules = new ArrayList<>();

		state = WebinterfaceState.STOPPED;

		rootDirectory = new File(Paths.get("").toAbsolutePath().toString());
		includePath = new File(rootDirectory, "include").toPath();
		accountStorage = null;
		sessionStorage = null;
		credentialsStorage = null;
		config = null;
		markdownRenderer = new MarkdownRenderer();
		webSocketEndpoint = new WebSocketDocument();
		setup = new Setup();

		registerActionHandler(new DefaultHandler());

		SQLHelper.cleanUp();
	}

	/**
	 * Initializes core components needed for any WIAPI installation
	 */
	private static void initialize() {
		config = new FileConfig(new File(getConfigurationDirectory(), "config.yml"));
		config.registerSettings(DefaultSettings.INSTANCE);

		Arrays.stream(DefaultJSModule.values()).forEach(Webinterface::registerJSModule);

		PHP.setEnabled(config.getSetting(DefaultSettings.ENABLE_PHP));
		PHP.setCGIPath(config.getSetting(DefaultSettings.PHP_CGI_PATH));
		PHP.setFileExtensions(config.getSetting(DefaultSettings.PHP_FILE_EXTENSIONS));

		httpServer = new HttpServer(HttpServer.newConfigurationBuilder()
			.host(config.getSetting(DefaultSettings.HTTP_BIND))
			.port(config.getSetting(DefaultSettings.HTTP_PORT))
			.debugMode(config.getSetting(DefaultSettings.ENABLE_DEBUG_MODE))
			.defaultCorsConfiguration(CorsConfiguration.createDefault()
				.allowAllOrigins(config.getSetting(DefaultSettings.CORS_ALLOW_ALL_ORIGINS))
				.addAllowedOrigins(config.getSetting(DefaultSettings.CORS_ALLOWED_ORIGINS).toArray(String[]::new))
				.addAllowedHeaders(config.getSetting(DefaultSettings.CORS_ALLOWED_HEADERS).toArray(String[]::new))
				.addExposedHeaders(config.getSetting(DefaultSettings.CORS_EXPOSED_HEADERS).toArray(String[]::new))
				.maxAge(config.getSetting(DefaultSettings.CORS_MAX_AGE))
				.allowCredentials(config.getSetting(DefaultSettings.CORS_ALLOW_CREDENTIALS))
				.sendAllAllowedMethods(config.getSetting(DefaultSettings.CORS_SEND_ALL_ALLOWED_METHODS))
				.addAllowedMethods(config.getSetting(DefaultSettings.CORS_ALLOWED_METHODS).stream().map(HttpRequestMethod::get).toArray(HttpRequestMethod[]::new)))
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

		extractResources("/webinterfaceapi-resources.list");
		documentProvider.registerPattern(HttpRequestMethod.GET, "/_internal/include/{path...}", new IncludedFilesDocument());
	}

	/**
	 * Internal method. Do not call manually
	 */
	public static void initializeDatabase() {
		switch(config.getSetting(DefaultSettings.DATABASE)) {
			case "file":
				accountStorage = new FileAccountStorage(new File(getDataDirectory(), "accounts.yml"));
				sessionStorage = new FileSessionStorage(new File(getDataDirectory(), "sessions.yml"));
				credentialsStorage = new FileCredentialsStorage(new File(getDataDirectory(), "credentials.yml"));
				break;
			case "sql":
				SQLHelper.initialize();
				accountStorage = new SQLAccountStorage();
				sessionStorage = new SQLSessionStorage();
				credentialsStorage = new SQLCredentialsStorage();
				break;
			default:
				throw new IllegalArgumentException("Invalid database configured in config");
		}

		accountStorage.initialize();
		sessionStorage.initialize();
		credentialsStorage.initialize();
	}

	/**
	 * Internal method. Do not call manually
	 */
	public static void registerWIContent() {
		registerAuthMethod(new NoAuth());
		registerAuthMethod(new DiscordAuth());
		registerAuthMethod(new GoogleAuth());
		registerAuthMethod(new GitHubAuth());
		registerAuthMethod(new PasswordAuth());

		documentProvider.register(HttpRequestMethod.POST, "/_internal/call", new ActionCallbackDocument());
		documentProvider.register(HttpRequestMethod.POST, "/_internal/fileupload", new FileUploadDocument());
		documentProvider.register(HttpRequestMethod.GET, "/_internal/ws", webSocketEndpoint);
		documentProvider.register(HttpRequestMethod.GET, "/", new HomeDocument());
		documentProvider.register(HttpRequestMethod.GET, "/login", new LoginDocument());
		documentProvider.register(HttpRequestMethod.GET, "/logout", new LogoutDocument());

		RegistrationSecretDocument secret = new RegistrationSecretDocument();
		documentProvider.register(HttpRequestMethod.GET, "/registration-secret", secret);
		documentProvider.register(HttpRequestMethod.POST, "/registration-secret", secret);
		if(config.getSetting(DefaultSettings.ENABLE_PASSWORD_AUTH)) documentProvider.register(HttpRequestMethod.GET, "/auth/password/login", new PasswordLoginDocument());

		PageCategory cat = createCategory("WebinterfaceAPI");
		cat.addPage(new WelcomePage());
		cat.addPage(new AccountsPage());
		cat.addPage(new PermissionsPage());
		cat.addPage(new AccountPage());
		cat.addPage(new DefaultSettingsPage());

		pages.forEach(page -> documentProvider.register(HttpRequestMethod.GET, page.getUrl(), page));
		categories.forEach(category -> category.getPages().forEach(page -> documentProvider.register(HttpRequestMethod.GET, page.getUrl(), page)));

		authMethods.forEach(Webinterface::registerAuthPages);
		jsModules.forEach(Webinterface::registerJSModulePage);

		authMethods.forEach(AuthMethod::initialize);
	}

	public static void start() {
		state = WebinterfaceState.STARTING;

		initialize();

		if(setup.isStepDone(DatabaseSetupStep.ID)) initializeDatabase();

		if(setup.getNextStep() != null) {
			// Setup is not done, only register setup-related stuff
			documentProvider.register(HttpRequestMethod.GET, "/", () -> {
				HttpServerHeader h = HttpRequestContext.getCurrentContext().getServerHeader();
				h.setStatusCode(HttpStatusCodes.SEE_OTHER_303);
				h.getFields().set("Location", "/setup");
			});
			documentProvider.register(HttpRequestMethod.GET, "/setup", new SetupDocument());
			documentProvider.register(HttpRequestMethod.POST, "/setup/submit", new SetupSubmitDocument());
		}else {
			registerWIContent();
		}

		try {
			httpServer.start();
			if(httpsServer != null) httpsServer.start();
		}catch(Exception e) {
			logger.error("Failed to start http(s) server", e);
			return;
		}

		taskScheduler.scheduleAtFixedRate(() -> {
			logger.debug("Cleaning up sessions");
			for(Session sess : getSessionStorage().getSessions()) {
				if(sess.hasExpired()) getSessionStorage().deleteSession(sess.getSessionID());
			}
		}, 10, 10, TimeUnit.MINUTES);

		state = WebinterfaceState.RUNNING;
	}

	public static WebinterfaceState getState() {
		return state;
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

			Path resourcesPath = getDataDirectory().toPath().resolve("resources.json");
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
						Webinterface.getLogger().warn("Resource file '" + resource + "' has been edited. Skipping!");
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

	public static HttpServer getHttpServer() {
		return httpServer;
	}

	public static HttpsServer getHttpsServer() {
		return httpsServer;
	}

	public static void setDocumentProvider(DocumentProvider documentProvider) {
		Webinterface.documentProvider = documentProvider;
	}

	public static DocumentProvider getDocumentProvider() {
		return documentProvider;
	}

	public static void setRootDirectory(File rootDirectory) {
		if(state != WebinterfaceState.STOPPED) throw new IllegalStateException("Webinterface is running");
		Webinterface.rootDirectory = rootDirectory;
		includePath = new File(rootDirectory, "include").toPath();
	}

	public static File getRootDirectory() {
		return rootDirectory;
	}

	public static Path getIncludePath() {
		return includePath;
	}

	public static File getConfigurationDirectory() {
		return new File(rootDirectory, "cfg/");
	}

	public static File getDataDirectory() {
		return new File(rootDirectory, "data/");
	}

	public static Config getConfig() {
		return config;
	}

	public static void registerPage(Page page) {
		pages.add(page);
		if(state == WebinterfaceState.RUNNING) documentProvider.register(HttpRequestMethod.GET, page.getUrl(), page);
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
		if(state == WebinterfaceState.RUNNING) {
			registerAuthPages(method);
			method.initialize();
		}
	}

	private static void registerAuthPages(AuthMethod method) {
		documentProvider.register(HttpRequestMethod.GET, "/auth/" + method.getID(), new AuthRequestDocument(method));
		documentProvider.register(HttpRequestMethod.POST, "/auth/" + method.getID() + "/response", new AuthResponseDocument(method));
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
		if(state == WebinterfaceState.RUNNING) registerJSModulePage(module);
	}

	private static void registerJSModulePage(JSModule module) {
		documentProvider.register(HttpRequestMethod.GET, "/_internal/module/" + module.getIdentifier() + ".js", module);
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

	public static Setup getSetup() {
		return setup;
	}

	public static void shutdown() {
		state = WebinterfaceState.STOPPING;
		if(httpServer != null) httpServer.shutdown();
		if(httpsServer != null) httpsServer.shutdown();
		if(taskScheduler != null) taskScheduler.shutdown();

		logger.info("Waiting for task scheduler");
		try {
			if(!taskScheduler.awaitTermination(30, TimeUnit.SECONDS)) taskScheduler.shutdownNow();
		} catch (InterruptedException e) {
			logger.warn("Interrupted during shutdown", e);
		}

		cleanUp();

		state = WebinterfaceState.STOPPED;
		logger.info("Shutdown done");
	}

	public static Logger getLogger() {
		return logger;
	}

}
