package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import org.commonmark.parser.Parser;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.markdown.MarkdownRenderer;

public class WebinterfaceText extends AbstractWebinterfacePageElement {
	
	private Supplier<String> text;
	private boolean enableMarkdown;
	
	public WebinterfaceText(Supplier<String> text) {
		this.text = text;
	}
	
	public WebinterfaceText(String text) {
		this(() -> text);
	}
	
	public void setText(Supplier<String> text) {
		this.text = text;
	}
	
	public void setText(String text) {
		setText(() -> text);
	}
	
	public Supplier<String> getText() {
		return text;
	}
	
	public void setEnableMarkdown(boolean enableMarkdown) {
		this.enableMarkdown = enableMarkdown;
	}
	
	public boolean isEnableMarkdown() {
		return enableMarkdown;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("a");
		if(enableMarkdown) {
			MarkdownRenderer r = new MarkdownRenderer();
			b.appendChild(r.render(Parser.builder().build().parse(text.get())));
		}else {
			b.setText(text);
		}
		return b;
	}
	
}
