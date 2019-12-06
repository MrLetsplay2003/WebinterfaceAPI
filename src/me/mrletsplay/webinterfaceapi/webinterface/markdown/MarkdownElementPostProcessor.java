package me.mrletsplay.webinterfaceapi.webinterface.markdown;

import org.commonmark.node.Node;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public interface MarkdownElementPostProcessor {

	public HtmlElement process(Node markdownNode, HtmlElement htmlElement);
	
}
