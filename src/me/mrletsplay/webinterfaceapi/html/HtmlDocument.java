package me.mrletsplay.webinterfaceapi.html;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.element.HtmlLink;
import me.mrletsplay.webinterfaceapi.html.element.HtmlScript;
import me.mrletsplay.webinterfaceapi.http.document.HttpDocument;
import me.mrletsplay.webinterfaceapi.http.request.HttpRequestContext;

public class HtmlDocument implements HttpDocument {

	private HtmlElement html, head, body, title, description;
	
	public HtmlDocument() {
		this.html = new HtmlElement("html");
		this.head = new HtmlElement("head");
		this.body = new HtmlElement("body");
		this.title = new HtmlElement("title");
		title.setText("Webinterface");
		this.description = new HtmlElement("meta");
		description.setAttribute("name", "description");
		description.setAttribute("content", "WebinterfaceAPI html document!");
		head.appendChild(title);
		head.appendChild(description);
		html.appendChild(head);
		html.appendChild(body);
	}
	
	public HtmlElement getParentNode() {
		return html;
	}
	
	public HtmlElement getHeadNode() {
		return head;
	}
	
	public HtmlElement getBodyNode() {
		return body;
	}
	
	public HtmlElement getTitleNode() {
		return title;
	}
	
	public HtmlElement getDescriptionNode() {
		return description;
	}
	
	public void addStyleSheet(String path) {
		HtmlLink el = HtmlElement.link();
		el.setRel("stylesheet");
		el.setLinkType("text/css");
		el.setHref(path);
		head.appendChild(el);
	}
	
	public void includeScript(String src, boolean async) {
		HtmlScript sc = HtmlElement.script();
		sc.setSource(src);
		sc.setAsync(async);
		head.appendChild(sc);
	}
	
	public void setTitle(Supplier<String> title) {
		this.title.setText(title);
	}
	
	public void setTitle(String title) {
		setTitle(() -> title);
	}
	
	public void setDescription(Supplier<String> description) {
		this.description.setAttribute("content", description);
	}
	
	public void setDescription(String description) {
		setDescription(() -> description);
	}
	
	public void setLanguage(String lang) {
		html.setAttribute("lang", lang);
	}
	
	@Override
	public void createContent() {
		HttpRequestContext.getCurrentContext().getServerHeader().setContent("text/html", ("<!DOCTYPE html>" + html.toString()).getBytes(StandardCharsets.UTF_8));
	}

}
