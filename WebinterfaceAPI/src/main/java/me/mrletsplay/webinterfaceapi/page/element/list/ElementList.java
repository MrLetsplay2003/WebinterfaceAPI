package me.mrletsplay.webinterfaceapi.page.element.list;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.dom.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.UpdateElementAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.data.DataHandler;
import me.mrletsplay.webinterfaceapi.page.element.AbstractPageElement;
import me.mrletsplay.webinterfaceapi.page.element.PageElement;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

/**
 *
 * @author mr
 *
 * @param <T> The type of items in this list. This must be a JSON-compatible type
 */
public class ElementList<T> extends AbstractPageElement {

	private Supplier<List<T>> items;

	private boolean
		rearrangable,
		removable;

	private PageElement templateElement;

	private DataHandler dataHandler;

	private Action onChange;

	public ElementList(PageElement templateElement) {
		this.templateElement = templateElement;
	}

	protected ElementList() {}

	/**
	 * Sets the initial items for the list
	 * @param items The initial items
	 */
	public void setInitialItems(Supplier<List<T>> items) {
		this.items = items;
	}

	/**
	 * Sets the initial items for the list
	 * @param items The initial items
	 */
	public void setInitialItems(List<T> items) {
		this.items = () -> items;
	}

	public Supplier<List<T>> getInitialItems() {
		return items;
	}

	/**
	 * Sets the template element for this list
	 * @param templateElement The template element
	 */
	public void setTemplateElement(PageElement templateElement) {
		this.templateElement = templateElement;
	}

	public PageElement getTemplateElement() {
		return templateElement;
	}

	public void setRearrangable(boolean rearrangable) {
		this.rearrangable = rearrangable;
	}

	public boolean isRearrangable() {
		return rearrangable;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	public boolean isRemovable() {
		return removable;
	}

	public void setDataHandler(DataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}

	public DataHandler getDataHandler() {
		return dataHandler;
	}

	public void setOnChangeAction(Action onChange) {
		this.onChange = onChange;
	}

	public Action getOnChange() {
		return onChange;
	}

	public ActionValue itemsValue() {
		return ActionValue.listItems(this);
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout");
		el.appendAttribute("style", "grid-template-columns: 1fr;");

		if(templateElement == null) throw new IllegalStateException("List doesn't have a template");

		if(!templateElement.isTemplate()) throw new IllegalStateException("Template element is not a template");
		el.addClass("dynamic-list");
		el.setAttribute("data-listItems", items == null ? "[]" : new JSONArray(items.get()).toString());
		if(dataHandler != null) {
			el.setAttribute("data-dataHandler", dataHandler.toObject().toJavaScript());
		}
		el.setAttribute("data-template", createDynamicListItem().toString());
		if(onChange != null) el.setAttribute("data-onChange", onChange.createAttributeValue());

		return el;
	}

	private HtmlElement createDynamicListItem() {
		HtmlElement container = new HtmlElement("div");
		container.addClass("list-item");
		container.appendChild(templateElement.toHtml());
		if(rearrangable) {
			HtmlButton upBtn = HtmlElement.button();
			upBtn.addClass("list-button list-button-up");
			upBtn.setOnClick("listSwapItems(this.parentElement, this.parentElement.previousElementSibling)");
			upBtn.appendChild(WebinterfaceUtils.iconifyIcon("mdi:chevron-up"));
			container.appendChild(upBtn);

			HtmlButton downBtn = HtmlElement.button();
			downBtn.addClass("list-button list-button-down");
			downBtn.setOnClick("listSwapItems(this.parentElement, this.parentElement.nextElementSibling)");
			downBtn.appendChild(WebinterfaceUtils.iconifyIcon("mdi:chevron-down"));
			container.appendChild(downBtn);
		}

		if(removable) {
			HtmlButton removeBtn = HtmlElement.button();
			removeBtn.addClass("list-button");
			removeBtn.setOnClick("listRemoveItem(this.parentElement)");
			removeBtn.appendChild(WebinterfaceUtils.iconifyIcon("mdi:delete"));
			container.appendChild(removeBtn);
		}
		return container;
	}

	public static <T> Builder<T, ?> builder() {
		return new Builder<>(new ElementList<>());
	}

	public static class Builder<T, S extends Builder<T, S>> extends AbstractElementBuilder<ElementList<T>, S> {

		protected Builder(ElementList<T> element) {
			super(element);
		}

		/**
		 * Sets the items used for the list
		 * @param items A list of initial items
		 * @return This builder
		 */
		public S initialItems(List<T> items) {
			element.setInitialItems(items);
			return getSelf();
		}

		/**
		 * Sets the items used for the list
		 * @param items A list of initial items
		 * @return This builder
		 */
		public S initialItems(Supplier<List<T>> items) {
			element.setInitialItems(items);
			return getSelf();
		}

		/**
		 * Sets the template element used for the elements of this list
		 * @param templateElement The template element to use
		 * @return This builder
		 */
		public S templateElement(PageElement templateElement) {
			element.setTemplateElement(templateElement);
			return getSelf();
		}

		/**
		 * Changes whether items in this list should be rearrangable (using up and down buttons)
		 * @param rearrangable Whether the items in this list should be rearrangable
		 * @return This builder
		 */
		public S rearrangable(boolean rearrangable) {
			element.setRearrangable(rearrangable);
			return getSelf();
		}

		/**
		 * Changes whether items in this list should be removable (using a delete button)
		 * @param removable Whether the items in this list should be removable
		 * @return This builder
		 */
		public S removable(boolean removable) {
			element.setRemovable(removable);
			return getSelf();
		}

		/**
		 * Sets a data handler for this list to retrieve data when it is updated (when initially loading or by using an {@link UpdateElementAction}
		 * @param dataHandler The data handler to use
		 * @return This builder
		 */
		public S dataHandler(DataHandler dataHandler) {
			element.setDataHandler(dataHandler);
			return getSelf();
		}

		public S onChange(Action onChange) {
			element.setOnChangeAction(onChange);
			return getSelf();
		}

		public S onChange(Function<? super ElementList<T>, Action> onChange) {
			element.setOnChangeAction(onChange.apply(element));
			return getSelf();
		}

		@Override
		public ElementList<T> create() throws IllegalStateException {
			if(element.getInitialItems() != null && element.getDataHandler() != null) throw new IllegalStateException("Items and data handler may not both be set");

			return super.create();
		}

	}

}
