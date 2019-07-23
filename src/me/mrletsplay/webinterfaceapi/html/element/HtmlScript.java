package me.mrletsplay.webinterfaceapi.html.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.HtmlElementFlag;

public class HtmlScript extends HtmlElement {

	public HtmlScript() {
		super("script");
		flags.add(HtmlElementFlag.DONT_ESCAPE_TEXT);
	}
	
	public void setSource(Supplier<String> src) {
		setAttribute("src", src);
	}
	
	public void setSource(String src) {
		setAttribute("src", src);
	}
	
	public Supplier<String> getSource() {
		return getAttribute("src");
	}
	
	public void setAsync(boolean async) {
		if(async) {
			setAttribute("async");
		}else {
			unsetAttribute("async");
		}
	}
	
	@Override
	protected HtmlElement copy(boolean deep) {
		HtmlScript br = new HtmlScript();
		applyAttributes(br, deep);
		return br;
	}
	
}
