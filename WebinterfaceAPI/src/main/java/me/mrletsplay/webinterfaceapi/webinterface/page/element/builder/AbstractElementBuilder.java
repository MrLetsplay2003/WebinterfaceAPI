package me.mrletsplay.webinterfaceapi.webinterface.page.element.builder;

import me.mrletsplay.mrcore.misc.Builder;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.Action;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.PageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.ElementLayoutOption;

public abstract class AbstractElementBuilder<T extends PageElement, S extends AbstractElementBuilder<T, S>> implements Builder<T, S> {

	protected T element;

	public AbstractElementBuilder(T element) {
		this.element = element;
	}

	public S align(Align align) {
		element.addLayoutOptions(align.getLayoutOptions());
		return getSelf();
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

	public S onClick(Action action) {
		element.setOnClickAction(action);
		return getSelf();
	}

	public S template(boolean isTemplate) {
		element.setTemplate(isTemplate);
		return getSelf();
	}

	@Override
	public T create() {
		return element;
	}

}
