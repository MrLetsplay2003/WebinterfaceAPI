package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceUpdateableElement extends AbstractWebinterfacePageElement {

	private WebinterfacePageElement templateElement;
	private String
		dataRequestTarget,
		dataRequestMethod;
	
	public WebinterfaceUpdateableElement(WebinterfacePageElement templateElement, String dataRequestTarget, String dataRequestMethod) {
		this.templateElement = templateElement;
		this.dataRequestTarget = dataRequestTarget;
		this.dataRequestMethod = dataRequestMethod;
	}
	
	private WebinterfaceUpdateableElement() {}
	
	public void setTemplateElement(WebinterfacePageElement templateElement) {
		this.templateElement = templateElement;
	}
	
	public WebinterfacePageElement getTemplateElement() {
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
		return new Builder(new WebinterfaceUpdateableElement());
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceUpdateableElement, Builder> {

		private Builder(WebinterfaceUpdateableElement element) {
			super(element);
		}
		
		public Builder templateElement(WebinterfacePageElement templateElement) {
			element.setTemplateElement(templateElement);
			return this;
		}
		
		public Builder dataHandler(String requestTarget, String requestMethod) {
			element.setDataHandler(requestTarget, requestMethod);
			return this;
		}
		
		@Override
		public WebinterfaceUpdateableElement create() {
			if(element.getTemplateElement() == null) throw new IllegalStateException("No template element set");
			if(element.getDataRequestTarget() == null || element.getDataRequestMethod() == null) throw new IllegalStateException("No data handler set");
			return super.create();
		}
		
	}

}
