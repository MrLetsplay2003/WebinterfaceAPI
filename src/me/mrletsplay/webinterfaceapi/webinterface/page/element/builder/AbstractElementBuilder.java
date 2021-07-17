package me.mrletsplay.webinterfaceapi.webinterface.page.element.builder;

import me.mrletsplay.mrcore.misc.Builder;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.ElementLayoutOption;

public abstract class AbstractElementBuilder<T extends WebinterfacePageElement, S extends AbstractElementBuilder<T, S>> implements Builder<T, S> {
	
	protected T element;
	
	public AbstractElementBuilder(T element) {
		this.element = element;
	}
	
	public S centered() {
		element.addLayoutOptions(DefaultLayoutOption.CENTER_TEXT);
		return getSelf();
	}
	
	public S centeredVertically() {
		element.addLayoutOptions(DefaultLayoutOption.CENTER_VERTICALLY);
		return getSelf();
	}
	
	public S leftbound() {
		element.addLayoutOptions(DefaultLayoutOption.LEFTBOUND);
		return getSelf();
	}
	
	public S rightbound() {
		element.addLayoutOptions(DefaultLayoutOption.RIGHTBOUND);
		return getSelf();
	}
	
	public S fullWidth() {
		element.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		return getSelf();
	}
	
	public S style(String key, String value) {
		element.getStyle().setProperty(key, value);
		return getSelf();
	}
	
	public S mobileStyle(String key, String value) {
		element.getMobileStyle().setProperty(key, value);
		return getSelf();
	}
	
	public S width(String width) {
		element.setWidth(width);
		return getSelf();
	}
	
	public S height(String height) {
		element.setHeight(height);
		return getSelf();
	}
	
	public S withLayoutOptions(ElementLayoutOption... layoutOptions) {
		element.addLayoutOptions(layoutOptions);
		return getSelf();
	}
	
	public T create() {
		return element;
	}

}
