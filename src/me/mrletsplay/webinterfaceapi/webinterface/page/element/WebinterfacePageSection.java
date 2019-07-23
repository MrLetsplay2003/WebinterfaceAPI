package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;

public class WebinterfacePageSection extends WebinterfaceElementGroup {

	@Override
	public HtmlElement createElement() {
		HtmlElement el = super.createElement();
//		el.setAttribute("style", "background-color:red");
		return el;
	}

}
