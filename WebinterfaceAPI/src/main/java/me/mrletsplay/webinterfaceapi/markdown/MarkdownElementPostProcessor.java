package me.mrletsplay.webinterfaceapi.markdown;

import org.commonmark.node.Node;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;

public interface MarkdownElementPostProcessor {

	public HtmlElement process(Node markdownNode, HtmlElement htmlElement);

}
