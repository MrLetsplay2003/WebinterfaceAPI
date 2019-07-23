package me.mrletsplay.webinterfaceapi.css.selector;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.HtmlException;

public class CssSelector {
	
	private Supplier<String> selectorString;
	
	public CssSelector(Supplier<String> selectorString) {
		this.selectorString = selectorString;
	}
	
	public CssSelector(String selectorString) {
		this(() -> selectorString);
	}
	
	@Override
	public String toString() {
		return selectorString.get();
	}
	
	public static CssSelector selectElement(HtmlElement element) {
		if(element.getID() == null) throw new HtmlException("Element needs to have an ID");
		return new CssSelector(() -> "#" + element.getID().get());
	}
	
	public static CssSelector selectClass(String htmlClass) {
		return new CssSelector("." + htmlClass);
	}
	
	public static CssSelector selectBody() {
		return new CssSelector("body");
	}
	
}
