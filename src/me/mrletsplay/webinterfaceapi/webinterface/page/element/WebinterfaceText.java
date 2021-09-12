package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import org.commonmark.parser.Parser;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.markdown.MarkdownElementPostProcessor;
import me.mrletsplay.webinterfaceapi.webinterface.markdown.MarkdownRenderer;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceText extends AbstractWebinterfacePageElement {
	
	private Supplier<String> text;
	private boolean enableMarkdown;
	private MarkdownElementPostProcessor markdownPostProcessor;
	
	public WebinterfaceText(Supplier<String> text) {
		this.text = text;
	}
	
	public WebinterfaceText(String text) {
		this(() -> text);
	}
	
	private WebinterfaceText() {}
	
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
	
	public void setMarkdownPostProcessor(MarkdownElementPostProcessor markdownPostProcessor) {
		this.markdownPostProcessor = markdownPostProcessor;
	}
	
	public MarkdownElementPostProcessor getMarkdownPostProcessor() {
		return markdownPostProcessor;
	}
	
	public ElementValue getValue() {
		return new ElementValue(this);
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("a");
		if(enableMarkdown) {
			MarkdownRenderer r = new MarkdownRenderer();
			if(markdownPostProcessor != null) r.setPostProcessor(markdownPostProcessor);
			b.appendChild(r.render(Parser.builder().build().parse(text.get())));
		}else {
			b.setText(text);
		}
		return b;
	}
	
	public static Builder builder() {
		return new Builder(new WebinterfaceText());
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceText, Builder> {

		private Builder(WebinterfaceText element) {
			super(element);
		}
		
		public Builder text(String text) {
			element.setText(text);
			return this;
		}
		
		public Builder text(Supplier<String> text) {
			element.setText(text);
			return this;
		}
		
		public Builder noLineBreaks() {
			element.getStyle().setProperty("white-space", "nowrap");
			return this;
		}
		
		public Builder enableMarkdown(boolean enableMarkdown) {
			element.setEnableMarkdown(enableMarkdown);
			return this;
		}
		
		public Builder markdownPostProcessor(MarkdownElementPostProcessor markdownPostProcessor) {
			element.setMarkdownPostProcessor(markdownPostProcessor);
			return this;
		}
		
		@Override
		public WebinterfaceText create() throws IllegalStateException {
			if(element.getText() == null) throw new IllegalStateException("No text set");
			return super.create();
		}
		
	}
	
}
