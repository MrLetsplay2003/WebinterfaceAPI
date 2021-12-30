package me.mrletsplay.webinterfaceapi.webinterface.page.element.builder;

import me.mrletsplay.mrcore.misc.Builder;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.ElementLayoutOption;

public abstract class AbstractElementBuilder<T extends WebinterfacePageElement, S extends AbstractElementBuilder<T, S>> implements Builder<T, S> {
	
	protected T element;
	
	public AbstractElementBuilder(T element) {
		this.element = element;
	}
	
	public S align(Align align) {
		element.addLayoutOptions(align.getLayoutOptions());
		return getSelf();
	}
	
	/**
	 * Use {@link #align(Align)} instead
	 * @return
	 */
	@Deprecated
	public S centered() {
		return align(Align.CENTER);
	}
	
	/**
	 * Use {@link #align(Align)} instead
	 * @return
	 */
	@Deprecated
	public S leftbound() {
		return align(Align.LEFT_CENTER);
	}

	/**
	 * Use {@link #align(Align)} instead
	 * @return
	 */
	@Deprecated
	public S rightbound() {
		return align(Align.RIGHT_CENTER);
	}
	
	public S leftboundText() {
		element.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
		return getSelf();
	}

	public S rightboundText() {
		element.addLayoutOptions(DefaultLayoutOption.RIGHTBOUND_TEXT);
		return getSelf();
	}

	public S justifyText() {
		element.addLayoutOptions(DefaultLayoutOption.JUSTIFY_TEXT);
		return getSelf();
	}

	public S centerText() {
		element.addLayoutOptions(DefaultLayoutOption.CENTER_TEXT);
		return getSelf();
	}
	
	public S noPadding() {
		element.addLayoutOptions(DefaultLayoutOption.NO_PADDING);
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
	
	public S onClick(WebinterfaceAction action) {
		element.setOnClickAction(action);
		return getSelf();
	}
	
	public S template(boolean isTemplate) {
		element.setTemplate(isTemplate);
		return getSelf();
	}
	
	public T create() {
		return element;
	}

}
