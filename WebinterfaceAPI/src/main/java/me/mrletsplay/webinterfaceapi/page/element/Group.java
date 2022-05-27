package me.mrletsplay.webinterfaceapi.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.page.data.DataHandler;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;

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

}
