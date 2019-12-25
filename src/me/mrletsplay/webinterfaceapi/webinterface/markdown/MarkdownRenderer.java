package me.mrletsplay.webinterfaceapi.webinterface.markdown;

import org.commonmark.node.BlockQuote;
import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
import org.commonmark.node.Document;
import org.commonmark.node.Emphasis;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Heading;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlInline;
import org.commonmark.node.Image;
import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.Link;
import org.commonmark.node.LinkReferenceDefinition;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.node.Paragraph;
import org.commonmark.node.SoftLineBreak;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.node.ThematicBreak;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class MarkdownRenderer {
	
	private MarkdownElementPostProcessor postProcessor;
	
	public void setPostProcessor(MarkdownElementPostProcessor postProcessor) {
		this.postProcessor = postProcessor;
	}
	
	public MarkdownElementPostProcessor getPostProcessor() {
		return postProcessor;
	}

	public HtmlElement render(Node node) {
		HtmlElement el;
		if(node instanceof Link) {
			Link l = (Link) node;
			el = new HtmlElement("a");
			el.setText(l.getTitle());
			el.setAttribute("href", l.getDestination());
		}else if(node instanceof Emphasis) {
			el = new HtmlElement("em");
		}else if(node instanceof StrongEmphasis) {
			el = new HtmlElement("b");
		}else if(node instanceof Code) {
			Code c = (Code) node;
			el = new HtmlElement("code");
			el.setText(c.getLiteral());
		}else if(node instanceof FencedCodeBlock) {
			FencedCodeBlock c = (FencedCodeBlock) node;
			el = new HtmlElement("code");
			el.setText(c.getLiteral());
		}else if(node instanceof HardLineBreak || node instanceof SoftLineBreak) {
			el = HtmlElement.br();
		}else if(node instanceof HtmlInline) {
			HtmlInline in = (HtmlInline) node;
			el = new HtmlElement("div");
			el.setText(in.getLiteral());
		}else if(node instanceof Text) {
			Text l = (Text) node;
			el = new HtmlElement("span");
			el.setText(l.getLiteral());
		}else if(node instanceof Image) {
			Image l = (Image) node;
			el = HtmlElement.img(l.getDestination(), l.getTitle());
		}else if(node instanceof LinkReferenceDefinition) {
			el = null;
		}else if(node instanceof Document){
			el = new HtmlElement("div");
		}else if(node instanceof Paragraph){
			el = new HtmlElement("p");
		}else if(node instanceof ThematicBreak){
			el = new HtmlElement("hr");
			el.setSelfClosing(true);
		}else if(node instanceof BulletList){
			el = new HtmlElement("ul");
		}else if(node instanceof OrderedList){
			el = new HtmlElement("ol");
		}else if(node instanceof ListItem){
			el = new HtmlElement("li");
		}else if(node instanceof BlockQuote){
			el = new HtmlElement("blockquote");
		}else if(node instanceof Heading){
			Heading h = (Heading) node;
			el = new HtmlElement("h" + h.getLevel());
		}else if(node instanceof HtmlBlock) {
			HtmlBlock in = (HtmlBlock) node;
			el = new HtmlElement("div");
			el.setText(in.getLiteral());
		}else if(node instanceof IndentedCodeBlock) {
			IndentedCodeBlock in = (IndentedCodeBlock) node;
			el = new HtmlElement("code");
			el.setText(in.getLiteral());
		}else {
			el = null;
		}
		
		if(el == null) return null;
		
		if(postProcessor != null) el = postProcessor.process(node, el);
		
		Node ch = node.getFirstChild();
		while(ch != null) {
			HtmlElement chEl = render(ch);
			if(chEl != null) el.appendChild(chEl);
			ch = ch.getNext();
		}
		
		return el;
	}

}
