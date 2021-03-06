package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.CheckboxValue;

public class WebinterfaceCheckBox extends AbstractWebinterfacePageElement {
	
	private WebinterfaceAction onChangeAction;
	private Supplier<Boolean> initialState;
	
	public WebinterfaceCheckBox(Supplier<Boolean> initialState) {
		this.initialState = initialState;
	}
	
	public WebinterfaceCheckBox(boolean initialState) {
		this(() -> initialState);
	}

	public WebinterfaceCheckBox() {
		this(() -> false);
	}
	
	public void setOnChangeAction(WebinterfaceAction onChangeAction) {
		this.onChangeAction = onChangeAction;
	}
	
	public Supplier<Boolean> getInitialState() {
		return initialState;
	}
	
	public CheckboxValue getCheckedValue() {
		return new CheckboxValue(this);
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement label = new HtmlElement("label");
		label.addClass("checkbox-container");
		HtmlElement ch = new HtmlElement("input");
		ch.setAttribute("type", "checkbox");
		ch.setAttribute("aria-label", "Yes/No"); // TODO aria-label
		if(initialState.get()) ch.setAttribute("checked");
		if(onChangeAction != null) ch.setAttribute("onchange", onChangeAction.createAttributeValue());
		label.appendChild(ch);
		HtmlElement sp = new HtmlElement("span");
		sp.addClass("checkbox-checkmark");
		label.appendChild(sp);
		return label;
	}

}
