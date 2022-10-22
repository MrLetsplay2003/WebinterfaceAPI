package me.mrletsplay.webinterfaceapi.page.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.page.data.DataHandler;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.page.element.layout.Grid;

public class Group extends AbstractPageElement {

	private List<PageElement> elements;

	private PageElement templateElement;

	private DataHandler dataHandler;

	public Group() {
		this.elements = new ArrayList<>();
		addLayoutOptions(DefaultLayoutOption.NO_PADDING);
	}

	public Group(PageElement templateElement) {
		this();
		this.templateElement = templateElement;
	}

	public void addTitle(Supplier<String> title) {
		TitleText t = new TitleText(title);
		t.setText(title);
		t.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addElement(t);
	}

	public void addTitle(String title) {
		addTitle(() -> title);
	}

	public void addElement(PageElement element) {
		elements.add(element);
	}

	public List<PageElement> getElements() {
		return elements;
	}

	public void setTemplateElement(PageElement templateElement) {
		this.templateElement = templateElement;
	}

	public PageElement getTemplateElement() {
		return templateElement;
	}

	public void setDataHandler(DataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}

	public DataHandler getDataHandler() {
		return dataHandler;
	}

	public boolean isDynamic() {
		return templateElement != null;
	}

	public void setGrid(Grid grid) {
		getStyle().setProperty("grid-template-columns", grid.getColumns() == null ? null : Arrays.stream(grid.getColumns()).collect(Collectors.joining(" ")));
		getMobileStyle().setProperty("grid-template-columns", "1fr");
		getStyle().setProperty("grid-template-rows", grid.getRows() == null ? null : Arrays.stream(grid.getRows()).collect(Collectors.joining(" ")));
		getStyle().setProperty("grid-gap", grid.getGap());
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout");

		if(templateElement != null) {
			if(!templateElement.isTemplate()) throw new IllegalStateException("Template element is not a template");
			el.addClass("dynamic-group");
			el.setAttribute("data-dataHandler", dataHandler.toObject().toJavaScript());
			el.setAttribute("data-template", templateElement.toHtml().toString());
		}else {
			for(PageElement e : elements) {
				el.appendChild(e.toHtml());
			}
		}

		return el;
	}

	public static Builder builder() {
		return new Builder(new Group());
	}

	public static class Builder extends AbstractElementBuilder<Group, Builder> {

		private Builder(Group element) {
			super(element);
		}

		public Builder title(String title) {
			element.addTitle(title);
			return this;
		}

		public Builder title(Supplier<String> title) {
			element.addTitle(title);
			return this;
		}

		public Builder addElement(PageElement element) {
			this.element.addElement(element);
			return this;
		}

		public Builder templateElement(PageElement templateElement) {
			this.element.setTemplateElement(templateElement);
			return this;
		}

		public Builder dataHandler(DataHandler dataHandler) {
			this.element.setDataHandler(dataHandler);
			return this;
		}

		public Builder grid(Grid grid) {
			this.element.setGrid(grid);
			return this;
		}

		@Override
		public Group create() {
			if(!element.getElements().isEmpty() && element.isDynamic()) throw new IllegalStateException("Data handler and elements may not both be set");

			return super.create();
		}

	}

}
