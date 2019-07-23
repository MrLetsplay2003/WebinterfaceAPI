package me.mrletsplay.webinterfaceapi.html.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class HtmlOption extends HtmlElement {

	public HtmlOption() {
		super("option");
	}
	
	public void setValue(Supplier<String> value) {
		setAttribute("value", value);
	}
	
	public void setValue(String value) {
		setAttribute("value", value);
	}
	
	public Supplier<String> getValue() {
		return getAttribute("value");
	}
	
	@Override
	protected HtmlElement copy(boolean deep) {
		HtmlOption br = new HtmlOption();
		applyAttributes(br, deep);
		return br;
	}
	
}
