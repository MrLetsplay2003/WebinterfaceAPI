package me.mrletsplay.webinterfaceapi.page.element;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.page.data.DataHandler;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;

public class UpdateableElement extends AbstractPageElement {

	private PageElement templateElement;
	private DataHandler dataHandler;

	public UpdateableElement(PageElement templateElement, DataHandler dataHandler) {
		this.templateElement = templateElement;
		this.dataHandler = dataHandler;
	}

	private UpdateableElement() {}

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

	@Override
	public HtmlElement createElement() {
		HtmlElement div = new HtmlElement("div");
		if(!templateElement.isTemplate()) throw new IllegalStateException("Template element is not a template");
		div.addClass("updateable-element");
		div.setAttribute("data-dataHandler", dataHandler.toObject().toJavaScript());
		HtmlElement templateHTML = templateElement.toHtml();
		div.setAttribute("data-template", templateHTML.toString());
		div.appendChild(templateHTML);
		return div;
	}

	public static Builder builder() {
		return new Builder(new UpdateableElement());
	}

	public static class Builder extends AbstractElementBuilder<UpdateableElement, Builder> {

		private Builder(UpdateableElement element) {
			super(element);
		}

		public Builder templateElement(PageElement templateElement) {
			element.setTemplateElement(templateElement);
			return this;
		}

		public Builder dataHandler(DataHandler dataHandler) {
			element.setDataHandler(dataHandler);
			return this;
		}

		@Override
		public UpdateableElement create() {
			if(element.getTemplateElement() == null) throw new IllegalStateException("No template element set");
			if(element.getDataHandler() == null) throw new IllegalStateException("No data handler set");
			return super.create();
		}

	}

}
