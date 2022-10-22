package me.mrletsplay.webinterfaceapi.page.element.layout;

import java.util.Arrays;
import java.util.stream.Collectors;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;

public class Grid {

	private String[] columns;
	private String[] rows;
	private String gap;

	public Grid() {
	}

	public Grid setColumns(String... columns) {
		this.columns = columns;
		return this;
	}

	public String[] getColumns() {
		return columns;
	}

	public Grid setRows(String... rows) {
		this.rows = rows;
		return this;
	}

	public String[] getRows() {
		return rows;
	}

	public Grid setGap(String gap) {
		this.gap = gap;
		return this;
	}

	public String getGap() {
		return gap;
	}

	public void apply(HtmlElement elementContainer, HtmlElement element) {
		if(columns != null) element.appendAttribute("style", "grid-template-columns: " + Arrays.stream(columns).collect(Collectors.joining(" ")) + ";");
		if(rows != null) element.appendAttribute("style", "grid-template-rows: " + Arrays.stream(rows).collect(Collectors.joining(" ")) + ";");
		if(gap != null) element.appendAttribute("style", "grid-gap: " + gap + ";");
	}

}