package me.mrletsplay.webinterfaceapi.html.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.HtmlElementFlag;

public class HtmlStyle extends HtmlElement {

	public HtmlStyle() {
		super("style");
		flags.add(HtmlElementFlag.DONT_ESCAPE_TEXT);
	}
	
	public void setMedia(Supplier<String> media) {
		setAttribute("media", media);
	}
	
	public void setMedia(String media) {
		setAttribute("media", media);
	}
	
	public Supplier<String> getMedia() {
		return getAttribute("media");
	}
	
	@Override
	protected HtmlElement copy(boolean deep) {
		HtmlStyle br = new HtmlStyle();
		applyAttributes(br, deep);
		return br;
	}
	
}
