package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.Base64;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class FileUpload extends AbstractPageElement {

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
		input.setSelfClosing(true);
		input.setAttribute("type", "file");
		input.setAttribute("name", "file");
		form.appendChild(input);
		return form;
	}

	public static Builder builder() {
		return new Builder(new FileUpload());
	}

	public static byte[] getUploadedFileBytes(ActionEvent event) {
		return Base64.getDecoder().decode(event.getRequestData().getString("value"));
	}

	public static class Builder extends AbstractElementBuilder<FileUpload, Builder> {

		private Builder(FileUpload element) {
			super(element);
		}

		public Builder uploadHandler(String requestTarget, String requestMethod) {
			element.setUploadHandler(requestTarget, requestMethod);
			return this;
		}

		@Override
		public FileUpload create() throws IllegalStateException {
			if(element.uploadRequestTarget == null || element.uploadRequestMethod == null) throw new IllegalStateException("Upload handler must be set");
			return super.create();
		}

	}

}
