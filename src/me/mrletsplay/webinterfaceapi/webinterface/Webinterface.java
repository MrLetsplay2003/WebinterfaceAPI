package me.mrletsplay.webinterfaceapi.webinterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.main.MrCoreServiceRegistry;
import me.mrletsplay.mrcore.misc.MiscUtils;
import me.mrletsplay.webinterfaceapi.http.HttpServer;
import me.mrletsplay.webinterfaceapi.http.HttpsServer;
import me.mrletsplay.webinterfaceapi.http.document.FileDocument;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocumentProvider;
import me.mrletsplay.webinterfaceapi.php.PHP;
import me.mrletsplay.webinterfaceapi.webinterface.auth.FileAccountStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.FileCredentialStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAuthMethod;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceCredentialsStorage;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.DiscordAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.GitHubAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.GoogleAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.NoAuth;
import me.mrletsplay.webinterfaceapi.webinterface.auth.impl.PasswordAuth;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.webinterface.config.WebinterfaceConfig;
import me.mrletsplay.webinterfaceapi.webinterface.config.WebinterfaceFileConfig;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceAuthRequestDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceAuthResponseDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceCallbackDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceDocumentProvider;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceLoginDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfaceLogoutDocument;
import me.mrletsplay.webinterfaceapi.webinterface.document.WebinterfacePasswordLoginDocument;
import me.mrletsplay.webinterfaceapi.webinterface.markdown.MarkdownRenderer;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageCategory;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.WebinterfaceAccountPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.WebinterfaceAccountsPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.WebinterfaceHomePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.impl.WebinterfaceSettingsPage;
import me.mrletsplay.webinterfaceapi.webinterface.session.FileSessionStorage;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSessionStorage;

public class Webinterface {
	
	private static final Logger LOGGER = Logger.getLogger(Webinterface.class.getPackage().getName());
	
	private static WebinterfaceService service;
	
	private static HttpDocumentProvider documentProvider;
	
	private static HttpServer httpServer;
	private static HttpsServer httpsServer;
	
	private static List<WebinterfacePage> pages;
	private static List<WebinterfacePageCategory> categories;
	private static List<WebinterfaceActionHandler> handlers;
	private static List<WebinterfaceAuthMethod> authMethods;
	private static Map<String, Map.Entry<File, Boolean>> includedFiles;
	private static WebinterfaceHomePage homePage;
	
	private static boolean initialized = false;
	private static File rootDirectory;
	private static WebinterfaceAccountStorage accountStorage;
	private static WebinterfaceSessionStorage sessionStorage;
	private static WebinterfaceCredentialsStorage credentialsStorage;
	private static WebinterfaceConfig config;
	private static MarkdownRenderer markdownRenderer;
	
	static {
		pages = new ArrayList<>();
		categories = new ArrayList<>();
		handlers = new ArrayList<>();
		authMethods = new ArrayList<>();
		includedFiles = new HashMap<>();
		rootDirectory = new File(Paths.get("").toAbsolutePath().toString());
		markdownRenderer = new MarkdownRenderer();
		
		WebinterfacePageCategory cat = createCategory("WebinterfaceAPI");
		homePage = new WebinterfaceHomePage();
		cat.addPage(homePage);
		cat.addPage(new WebinterfaceSettingsPage());
		cat.addPage(new WebinterfaceAccountsPage());
		cat.addPage(new WebinterfaceAccountPage());
		
		registerActionHandler(new DefaultHandler());
		
		service = new WebinterfaceService();
		MrCoreServiceRegistry.registerService("WebinterfaceAPI", service);
	}
	
	public static void initialize() {
		if(initialized) return;
		initialized = true;
		
		service.fire(WebinterfaceService.EVENT_PRE_INIT);
		
		includeFile("/_internal", new File(rootDirectory, "include"));
		accountStorage = new FileAccountStorage(new File(rootDirectory, "data/accounts.yml"));
		sessionStorage = new FileSessionStorage(new File(rootDirectory, "data/sessions.yml"));
		credentialsStorage = new FileCredentialStorage(new File(rootDirectory, "data/credentials.yml"));
		
		config = new WebinterfaceFileConfig(new File(getConfigurationDirectory(), "config.yml"));
		config.registerSettings(DefaultSettings.INSTANCE);
		
		if(config.getSetting(DefaultSettings.ALLOW_ANONYMOUS)) registerAuthMethod(new NoAuth());
		if(config.getSetting(DefaultSettings.ENABLE_DISCORD_AUTH)) registerAuthMethod(new DiscordAuth());
		if(config.getSetting(DefaultSettings.ENABLE_GOOGLE_AUTH)) registerAuthMethod(new GoogleAuth());
		if(config.getSetting(DefaultSettings.ENABLE_GITHUB_AUTH)) registerAuthMethod(new GitHubAuth());
		if(config.getSetting(DefaultSettings.ENABLE_PASSWORD_AUTH)) registerAuthMethod(new PasswordAuth());
		
		PHP.setEnabled(config.getSetting(DefaultSettings.ENABLE_PHP));
		PHP.setCGIPath(config.getSetting(DefaultSettings.PHP_CGI_PATH));
		PHP.setFileExtensions(config.getSetting(DefaultSettings.PHP_FILE_EXTENSIONS));
		
		documentProvider = new WebinterfaceDocumentProvider();
		
		httpServer = new HttpServer(config.getSetting(DefaultSettings.HTTP_BIND), config.getSetting(DefaultSettings.HTTP_PORT));
		httpServer.setDocumentProvider(documentProvider);
		
		if(config.getSetting(DefaultSettings.HTTPS_ENABLE)) {
			String certPath = config.getSetting(DefaultSettings.HTTPS_CERTIFICATE_PATH);
			String certKeyPath = config.getSetting(DefaultSettings.HTTPS_CERTIFICATE_KEY_PATH);
			
			if(certPath == null || certKeyPath == null) {
				LOGGER.warning("Both certificate and certificate key need to be configured to use HTTPS");
			}else {
				File certFile = new File(certPath);
				File certKeyFile = new File(certKeyPath);
				String password = config.getSetting(DefaultSettings.HTTPS_CERTIFICATE_PASSWORD);
				httpsServer = new HttpsServer(config.getSetting(DefaultSettings.HTTPS_BIND), config.getSetting(DefaultSettings.HTTPS_PORT), certFile, certKeyFile, password);
				httpsServer.setDocumentProvider(documentProvider);
			}
		}
		
		loadIncludedFiles();
		
		documentProvider.registerDocument("/favicon.ico", new FileDocument(new File(rootDirectory, "include/favicon.ico")));
		documentProvider.registerDocument("/_internal/call", new WebinterfaceCallbackDocument());
		documentProvider.registerDocument("/login", new WebinterfaceLoginDocument());
		documentProvider.registerDocument("/logout", new WebinterfaceLogoutDocument());
		if(config.getSetting(DefaultSettings.ENABLE_PASSWORD_AUTH)) documentProvider.registerDocument("/auth/password/login", new WebinterfacePasswordLoginDocument());
		
		pages.forEach(page -> documentProvider.registerDocument(page.getUrl(), page));
		categories.forEach(category -> category.getPages().forEach(page -> documentProvider.registerDocument(page.getUrl(), page)));
		
		authMethods.forEach(method -> {
			documentProvider.registerDocument("/auth/" + method.getID(), new WebinterfaceAuthRequestDocument(method));
			documentProvider.registerDocument("/auth/" + method.getID() + "/response", new WebinterfaceAuthResponseDocument(method));
		});
		
		service.fire(WebinterfaceService.EVENT_POST_INIT);
	}
	
	public static void start() {
		service.fire(WebinterfaceService.EVENT_PRE_START);
		
		extractFiles();
		initialize();
		
		accountStorage.initialize();
		sessionStorage.initialize();
		credentialsStorage.initialize();
		httpServer.start();
		if(httpsServer != null) httpsServer.start();
		
		httpServer.getExecutor().execute(() -> {
			Supplier<Boolean> keepRunning = () -> httpServer.isRunning() && !httpServer.getExecutor().isShutdown() && !Thread.interrupted();
			while(keepRunning.get()) {
				for(WebinterfaceSession sess : getSessionStorage().getSessions()) {
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
						Webinterface.getLogger().log(Level.FINE, "Extracting " + e.getName() + "...");
						IOUtils.createFile(ofl);
						try (InputStream in = fl.getInputStream(e);
								OutputStream out = new FileOutputStream(ofl)) {
							IOUtils.transfer(in, out);
						}
					}
				}
			}catch(IOException e) {
				Webinterface.getLogger().log(Level.FINE, "Error while extracting required files", e);
			}
		} catch (Exception e) {
			Webinterface.getLogger().log(Level.FINE, "Error while locating required files", e);
		}
	}
	
	@Deprecated
	public static HttpServer getServer() {
		return httpServer;
	}
	
	@NotNull
	public static HttpServer getHttpServer() {
		return httpServer;
	}
	
	@Nullable
	public static HttpsServer getHttpsServer() {
		return httpsServer;
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
	
	public static WebinterfaceConfig getConfig() {
		return config;
	}
	
	public static void registerPage(WebinterfacePage page) {
		pages.add(page);
	}
	
	public static List<WebinterfacePage> getPages() {
		return pages;
	}
	
	public static WebinterfaceHomePage getHomePage() {
		return homePage;
	}
	
	public static void registerCategory(WebinterfacePageCategory category) {
		categories.add(category);
	}
	
	public static WebinterfacePageCategory createCategory(String name) {
		WebinterfacePageCategory cat = new WebinterfacePageCategory(name);
		registerCategory(cat);
		return cat;
	}
	
	public static List<WebinterfacePageCategory> getCategories() {
		return categories;
	}
	
	public static void registerActionHandler(WebinterfaceActionHandler handler) {
		handlers.add(handler);
	}
	
	public static List<WebinterfaceActionHandler> getActionHandlers() {
		return handlers;
	}
	
	public static void registerAuthMethod(WebinterfaceAuthMethod method) {
		if(authMethods.stream().anyMatch(a -> a.getID().equals(method.getID()))) return;
		authMethods.add(method);
	}
	
	public static List<WebinterfaceAuthMethod> getAuthMethods() {
		return authMethods;
	}
	
	public static List<WebinterfaceAuthMethod> getAvailableAuthMethods() {
		return authMethods.stream().filter(WebinterfaceAuthMethod::isAvailable).collect(Collectors.toList());
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
	
	public static void includeFile(String path, File file, boolean includeFileName) {
		includedFiles.put(path, MiscUtils.newMapEntry(file, includeFileName));
	}
	
	public static void includeFile(String path, File file) {
		includeFile(path, file, false);
	}
	
	public static void loadIncludedFiles() {
		for(Map.Entry<String, Map.Entry<File, Boolean>> v : includedFiles.entrySet()) {
			documentProvider.registerFileDocument(v.getKey(), v.getValue().getKey(), v.getValue().getValue());
		}
	}
	
	public static Map<String, Map.Entry<File, Boolean>> getIncludedFiles() {
		return includedFiles;
	}
	
	public static void shutdown() {
		initialized = false;
		if(httpServer != null) httpServer.shutdown();
		if(httpsServer != null) httpsServer.shutdown();
	}
	
	public static Logger getLogger() {
		return LOGGER;
	}

}
