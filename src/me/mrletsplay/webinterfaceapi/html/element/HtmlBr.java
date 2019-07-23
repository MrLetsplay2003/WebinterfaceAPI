package me.mrletsplay.webinterfaceapi.html.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class HtmlBr extends HtmlElement {

	public HtmlBr() {
		super("br");
		setSelfClosing(true);
	}
	
	@Override
	protected HtmlElement copy(boolean deep) {
		HtmlBr br = new HtmlBr();
		applyAttributes(br, deep);
		return br;
	}
	
}
