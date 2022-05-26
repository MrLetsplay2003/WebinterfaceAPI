package me.mrletsplay.webinterfaceapi.page.element.list;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.dom.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.UpdateElementAction;
import me.mrletsplay.webinterfaceapi.page.element.AbstractPageElement;
import me.mrletsplay.webinterfaceapi.page.element.PageElement;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class ElementList<T> extends AbstractPageElement {

	private List<T> items;

	private Function<T, Object> toJSON;

	private boolean
		rearrangable,
		removable;

	private PageElement templateElement;

	private String
		dataRequestTarget,
		dataRequestMethod;

	private Action onChange;

	public ElementList(PageElement templateElement, Function<T, Object> toJSON) {
		this.templateElement = templateElement;
		this.toJSON = toJSON;
	}

	protected ElementList() {}

	/**
	 * Sets the initial items for the list
	 * @param items The initial items
	 */
	public void setInitialItems(List<T> items) {
		this.items = items;
	}

	public List<T> getItems() {
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

	public void setToJSONFunction(Function<T, Object> toJSON) {
		this.toJSON = toJSON;
	}

	public Function<T, Object> getToJSONFunction() {
		return toJSON;
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

	public void setOnChangeAction(Action onChange) {
		this.onChange = onChange;
	}

	public Action getOnChange() {
		return onChange;
	}

	public static <T> ActionResponse handleItems(List<T> items, Function<T, Object> toJSON) {
		JSONObject res = new JSONObject();
		res.put("items", items.stream().map(toJSON::apply).collect(Collectors.toCollection(JSONArray::new)));
		return ActionResponse.success(res);
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout");
		el.appendAttribute("style", "grid-template-columns: 1fr;");

		if(templateElement == null) throw new IllegalStateException("List doesn't have a template");

		if(!templateElement.isTemplate()) throw new IllegalStateException("Template element is not a template");
		el.addClass("dynamic-list");
		el.setAttribute("data-listItems", items == null ? "[]" : items.stream().map(toJSON::apply).collect(Collectors.toCollection(JSONArray::new)).toString());
		if(dataRequestTarget != null && dataRequestMethod != null) {
			el.setAttribute("data-dataRequestTarget", dataRequestTarget);
			el.setAttribute("data-dataRequestMethod", dataRequestMethod);
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
			upBtn.setOnClick("dynamicListElementSwap(this.parentElement, this.parentElement.previousElementSibling)");
			upBtn.appendChild(WebinterfaceUtils.iconifyIcon("mdi:chevron-up"));
			container.appendChild(upBtn);

			HtmlButton downBtn = HtmlElement.button();
			downBtn.addClass("list-button list-button-down");
			downBtn.setOnClick("dynamicListElementSwap(this.parentElement, this.parentElement.nextElementSibling)");
			downBtn.appendChild(WebinterfaceUtils.iconifyIcon("mdi:chevron-down"));
			container.appendChild(downBtn);
		}

		if(removable) {
			HtmlButton removeBtn = HtmlElement.button();
			removeBtn.addClass("list-button");
			removeBtn.setOnClick("dynamicListElementRemove(this.parentElement)");
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
		 * Sets the function that will be used to convert the initial list items to a JSON compatible type
		 * @param toJSON The function to use
		 * @return This builder
		 * @see #initialItems(List)
		 */
		public S toJSONFunction(Function<T, Object> toJSON) {
			element.setToJSONFunction(toJSON);
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
		 * @param requestTarget The request target of the data handler
		 * @param requestMethod The request method of the data handler
		 * @return This builder
		 */
		public S dataHandler(String requestTarget, String requestMethod) {
			element.setDataHandler(requestTarget, requestMethod);
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
			if(element.getItems() != null && (element.getDataRequestTarget() != null || element.getDataRequestMethod() != null)) throw new IllegalStateException("Items and data handler may not both be set");
			if(element.getItems() != null && element.getToJSONFunction() == null) throw new IllegalStateException("To JSON function must be set if the list contains initial items");

			return super.create();
		}

	}

}
