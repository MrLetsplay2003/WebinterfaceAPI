package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.Base64;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceFileUpload extends AbstractWebinterfacePageElement {
	
	private String
		uploadRequestTarget,
		uploadRequestMethod;
	
	public void setUploadHandler(String requestTarget, String requestMethod) {
		this.uploadRequestTarget = requestTarget;
		this.uploadRequestMethod = requestMethod;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement form = new HtmlElement("form");
		form.setAttribute("data-requestTarget", uploadRequestTarget);
		form.setAttribute("data-requestMethod", uploadRequestMethod);
		HtmlElement input = new HtmlElement("input");
		input.setAttribute("type", "file");
		input.setAttribute("name", "file");
		form.appendChild(input);
		return form;
	}
	
	public static Builder builder() {
		return new Builder(new WebinterfaceFileUpload());
	}
	
	public static byte[] getUploadedFileBytes(WebinterfaceRequestEvent event) {
		return Base64.getDecoder().decode(event.getRequestData().getString("value"));
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceFileUpload, Builder> {

		private Builder(WebinterfaceFileUpload element) {
			super(element);
		}
		
		public Builder uploadHandler(String requestTarget, String requestMethod) {
			element.setUploadHandler(requestTarget, requestMethod);
			return this;
		}
		
		@Override
		public WebinterfaceFileUpload create() throws IllegalStateException {
			if(element.uploadRequestTarget == null || element.uploadRequestMethod == null) throw new IllegalStateException("Upload handler must be set");
			return super.create();
		}
		
	}

}
