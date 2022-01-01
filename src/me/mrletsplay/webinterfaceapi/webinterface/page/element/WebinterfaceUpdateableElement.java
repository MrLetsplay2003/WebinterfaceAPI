package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceUpdateableElement extends AbstractWebinterfacePageElement {

	private WebinterfacePageElement templateElement;
	private String
		updateRequestTarget,
		updateRequestMethod;
	
	public WebinterfaceUpdateableElement(WebinterfacePageElement templateElement, String updateRequestTarget, String updateRequestMethod) {
		this.templateElement = templateElement;
		this.updateRequestTarget = updateRequestTarget;
		this.updateRequestMethod = updateRequestMethod;
	}
	
	private WebinterfaceUpdateableElement() {}
	
	public void setTemplateElement(WebinterfacePageElement templateElement) {
		this.templateElement = templateElement;
	}
	
	public WebinterfacePageElement getTemplateElement() {
		return templateElement;
	}
	
	public void setUpdateRequestTarget(String updateRequestTarget) {
		this.updateRequestTarget = updateRequestTarget;
	}
	
	public String getUpdateRequestTarget() {
		return updateRequestTarget;
	}
	
	public void setUpdateRequestMethod(String updateRequestMethod) {
		this.updateRequestMethod = updateRequestMethod;
	}
	
	public String getUpdateRequestMethod() {
		return updateRequestMethod;
	}

	@Override
	public HtmlElement createElement() {
		getOrGenerateID();
		if(!templateElement.isTemplate()) throw new IllegalStateException("Template element is not a template");
		HtmlElement el = templateElement.toHtml();
		el.addClass("updateable-element");
		el.setAttribute("data-updateRequestTarget", updateRequestTarget);
		el.setAttribute("data-updateRequestMethod", updateRequestMethod);
		el.setAttribute("data-template", templateElement.toHtml().toString());
		return el;
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
		
		public Builder updateHandler(String updateRequestTarget, String updateRequestMethod) {
			element.setUpdateRequestTarget(updateRequestTarget);
			element.setUpdateRequestMethod(updateRequestMethod);
			return this;
		}
		
		@Override
		public WebinterfaceUpdateableElement create() {
			if(element.getTemplateElement() == null) throw new IllegalStateException("No template element set");
			if(element.getUpdateRequestTarget() == null || element.getUpdateRequestMethod() == null) throw new IllegalStateException("No update handler set");
			return super.create();
		}
		
	}

}
