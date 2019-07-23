package me.mrletsplay.webinterfaceapi.html.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class HtmlButton extends HtmlElement {

	public HtmlButton() {
		super("button");
	}
	
	public void setOnClick(Supplier<String> onClick) {
		setAttribute("onclick", onClick);
	}
	
	public void setOnClick(String onClick) {
		setAttribute("onclick", onClick);
	}
	
	public Supplier<String> getOnClick() {
		return getAttribute("onclick");
	}
	
}
