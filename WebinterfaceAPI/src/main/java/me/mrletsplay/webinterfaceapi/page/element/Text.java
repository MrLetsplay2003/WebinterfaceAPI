package me.mrletsplay.webinterfaceapi.page.element;

import java.util.function.Supplier;

import org.commonmark.parser.Parser;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.markdown.MarkdownElementPostProcessor;
import me.mrletsplay.webinterfaceapi.markdown.MarkdownRenderer;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;

public class Text extends AbstractPageElement {

	private Supplier<String> text;
	private boolean enableMarkdown;
	private MarkdownElementPostProcessor markdownPostProcessor;

	public Text(Supplier<String> text) {
		this.text = text;
	}

	public Text(String text) {
		this(() -> text);
	}

	private Text() {}

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

	@Override
	public HtmlElement createElement() {
		if(enableMarkdown) {
			HtmlElement b = new HtmlElement("div");
			MarkdownRenderer r = new MarkdownRenderer();
			if(markdownPostProcessor != null) r.setPostProcessor(markdownPostProcessor);
			b.appendChild(r.render(Parser.builder().build().parse(text.get())));
			return b;
		}else {
			HtmlElement b = new HtmlElement("a");
			b.setText(text);
			return b;
		}
	}

	public static Builder builder() {
		return new Builder(new Text());
	}

	public static class Builder extends AbstractElementBuilder<Text, Builder> {

		private Builder(Text element) {
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
		public Text create() throws IllegalStateException {
			if(element.getText() == null) throw new IllegalStateException("No text set");
			return super.create();
		}

	}

}
