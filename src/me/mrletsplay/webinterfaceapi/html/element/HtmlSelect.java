package me.mrletsplay.webinterfaceapi.html.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class HtmlSelect extends HtmlElement {

	public HtmlSelect() {
		super("select");
	}
	
	public void addOption(String name, String value) {
		HtmlOption op = new HtmlOption();
		op.setText(name);
		op.setValue(value);
		appendChild(op);
	}
	
	@Override
	protected HtmlElement copy(boolean deep) {
		HtmlSelect br = new HtmlSelect();
		applyAttributes(br, deep);
		return br;
	}
	
}
