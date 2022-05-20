package me.mrletsplay.webinterfaceapi.page.element.layout;

import java.util.Arrays;
import java.util.stream.Collectors;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;

public class GridLayout implements ElementLayoutOption {

	private String[] columns;

	public GridLayout(String... columns) {
		this.columns = columns;
	}

	@Override
	public void apply(HtmlElement elementContainer, HtmlElement element) {
		element.appendAttribute("style", "grid-template-columns: " + Arrays.stream(columns).collect(Collectors.joining(" ")) + ";");
	}

}