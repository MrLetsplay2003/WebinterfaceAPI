package me.mrletsplay.webinterfaceapi.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import me.mrletsplay.simplehttpserver.dom.css.StyleSheet;
import me.mrletsplay.simplehttpserver.dom.html.HtmlDocument;
import me.mrletsplay.simplehttpserver.dom.js.JSScript;
import me.mrletsplay.simplehttpserver.http.header.HttpURLPath;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.webinterfaceapi.js.JSModule;

public class WebinterfaceContext {

	public static final String CONTEXT_PROPERTY_NAME = "webinterface-context";

	private HttpRequestContext httpContext;
	private HtmlDocument document;
	private JSScript script;
	private StyleSheet style;
	private Set<JSModule> requiredModules;

	public WebinterfaceContext(HttpRequestContext httpContext, HtmlDocument document, JSScript script, StyleSheet style) {
		this.httpContext = httpContext;
		this.document = document;
		this.script = script;
		this.style = style;
		this.requiredModules = new HashSet<>();
	}

	/**
	 * Returns the {@link HtmlDocument} of the current page
	 * @return The document of the current page
	 */
	public HtmlDocument getDocument() {
		return document;
	}

	/**
	 * Returns the global script of the current page
	 * @return The global script of the current page
	 */
	public JSScript getScript() {
		return script;
	}

	/**
	 * Returns the global stylesheet of the current page
	 * @return The global stylesheet of the current page
	 */
	public StyleSheet getStyle() {
		return style;
	}

	/**
	 * Returns the set of required modules of the current page
	 * @return The set of required modules of the current page
	 */
	public Set<JSModule> getRequiredModules() {
		return requiredModules;
	}

	/**
	 * Adds a JavaScript module to be included in the page
	 * @param module The module to be included
	 */
	public void requireModule(JSModule module) {
		requiredModules.add(module);
	}

	/**
	 * Adds multiple JavaScript module to be included in the page
	 * @param module The modules to be included
	 */
	public void requireModules(Collection<? extends JSModule> modules) {
		requiredModules.addAll(modules);
	}

	/**
	 * Adds multiple JavaScript module to be included in the page
	 * @param module The modules to be included
	 */
	public void requireModules(JSModule... modules) {
		requiredModules.addAll(Arrays.asList(modules));
	}

	/**
	 * Includes a script from the "include" resource folder in the page.<br>
	 * Note: To include a JavaScript module, use {@link #requireModule(JSModule)} instead
	 * @param path The path to the script relative to the <code>include/js</code> resource folder
	 * @see #requireModule(JSModule)
	 */
	public void includeScript(String path) {
		document.includeScript("/_internal/include/js/" + path, false, true);
	}

	/**
	 * Includes a style sheet from the "include" resource folder in the page
	 * @param path The path to the style sheet relative to the <code>include/css</code> resource folder
	 */
	public void includeStyleSheet(String path) {
		document.addStyleSheet("/_internal/include/css/" + path);
	}

	/**
	 * Returns the HTTP context for this request
	 * @return The HTTP context
	 */
	public HttpRequestContext getHttpContext() {
		return httpContext;
	}

	/**
	 * Returns the requested path
	 * @return The requested path
	 */
	public HttpURLPath getRequestedPath() {
		return httpContext.getRequestedPath();
	}

	/**
	 * Returns the context for the current request
	 * @return The context for the current request
	 */
	public static WebinterfaceContext getCurrentContext() {
		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		if(ctx == null) return null;
		return (WebinterfaceContext) ctx.getProperty(CONTEXT_PROPERTY_NAME);
	}

}
