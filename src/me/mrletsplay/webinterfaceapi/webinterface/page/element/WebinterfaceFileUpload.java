package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.Base64;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;

public class WebinterfaceFileUpload extends AbstractWebinterfacePageElement {
	
	private String
		uploadRequestTarget,
		uploadRequestMethod;
	
	public WebinterfaceFileUpload() {
		
	}
	
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
	
	public static byte[] getUploadedFileBytes(WebinterfaceRequestEvent event) {
		return Base64.getDecoder().decode(event.getRequestData().getString("value"));
	}

}
