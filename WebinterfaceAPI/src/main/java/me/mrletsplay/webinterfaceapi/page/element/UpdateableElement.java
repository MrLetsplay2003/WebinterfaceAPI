package me.mrletsplay.webinterfaceapi.page.element;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;

public class UpdateableElement extends AbstractPageElement {

	private PageElement templateElement;
	private String
		dataRequestTarget,
		dataRequestMethod;

	public UpdateableElement(PageElement templateElement, String dataRequestTarget, String dataRequestMethod) {
		this.templateElement = templateElement;
		this.dataRequestTarget = dataRequestTarget;
		this.dataRequestMethod = dataRequestMethod;
	}

	private UpdateableElement() {}

	public void setTemplateElement(PageElement templateElement) {
		this.templateElement = templateElement;
	}

	public PageElement getTemplateElement() {
		return templateElement;
	}

	public void setDataHandler(String requestTarget, String requestMethod) {
		this.dataRequestTarget = requestTarget;
		this.dataRequestMethod = requestMethod;
	}

	public String getDataRequestTarget() {
		return dataRequestTarget;
	}

	public String getDataRequestMethod() {
		return dataRequestMethod;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement div = new HtmlElement("div");
		if(!templateElement.isTemplate()) throw new IllegalStateException("Template element is not a template");
		div.addClass("updateable-element");
		div.setAttribute("data-dataRequestTarget", dataRequestTarget);
		div.setAttribute("data-dataRequestMethod", dataRequestMethod);
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

		public Builder dataHandler(String requestTarget, String requestMethod) {
			element.setDataHandler(requestTarget, requestMethod);
			return this;
		}

		@Override
		public UpdateableElement create() {
			if(element.getTemplateElement() == null) throw new IllegalStateException("No template element set");
			if(element.getDataRequestTarget() == null || element.getDataRequestMethod() == null) throw new IllegalStateException("No data handler set");
			return super.create();
		}

	}

}
