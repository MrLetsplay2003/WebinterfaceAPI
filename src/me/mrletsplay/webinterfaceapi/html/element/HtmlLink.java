package me.mrletsplay.webinterfaceapi.html.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class HtmlLink extends HtmlElement {

	public HtmlLink() {
		super("link");
		setSelfClosing(true);
	}
	
	public void setRel(Supplier<String> rel) {
		setAttribute("rel", rel);
	}
	
	public void setRel(String rel) {
		setAttribute("rel", rel);
	}
	
	public Supplier<String> getRel() {
		return getAttribute("rel");
	}
	
	public void setHref(Supplier<String> href) {
		setAttribute("href", href);
	}
	
	public void setHref(String href) {
		setAttribute("href", href);
	}
	
	public Supplier<String> getHref() {
		return getAttribute("href");
	}
	
	public void setLinkType(Supplier<String> type) {
		setAttribute("type", type);
	}
	
	public void setLinkType(String type) {
		setAttribute("type", type);
	}
	
	public Supplier<String> getLinkType() {
		return getAttribute("type");
	}
	
	@Override
	protected HtmlElement copy(boolean deep) {
		HtmlLink br = new HtmlLink();
		applyAttributes(br, deep);
		return br;
	}
	
}
