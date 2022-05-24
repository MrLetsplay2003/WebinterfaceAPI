package me.mrletsplay.webinterfaceapi.page.element.list;

import java.util.function.Function;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.simplehttpserver.dom.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.page.action.UpdateElementAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.page.element.AbstractPageElement;
import me.mrletsplay.webinterfaceapi.page.element.PageElement;
import me.mrletsplay.webinterfaceapi.page.element.builder.AbstractElementBuilder;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;

public class WebinterfaceElementList<T> extends AbstractPageElement {

	private ListAdapter<T> items;

	private Function<T, PageElement> elementFunction;

	private boolean
		rearrangable,
		removable;

	private String
		updateRequestTarget,
		updateRequestMethod;

	private PageElement templateElement;

	private String
		dataRequestTarget,
		dataRequestMethod;

	/**
	 * Constructs a list element with the items from the specified {@link ListAdapter}.<br>
	 * This will <b>not</b> automatically handle updates. You still need to set an update handler using {@link #setDataHandler(String, String)}, if you want to allow rearranging/remove items
	 * @param items The items to add to the list
	 * @param elementFunction A function to convert the elements to a {@link PageElement}
	 */
	public WebinterfaceElementList(ListAdapter<T> items, Function<T, PageElement> elementFunction) {
		this.items = items;
		this.elementFunction = elementFunction;
	}

	/**
	 * Constructs a list element with the items from the specified {@link ListAdapter}.<br>
	 * This will <b>not</b> automatically handle updates. You still need to set an update handler using {@link #setDataHandler(String, String)}, if you want to allow rearranging/remove items.<br>
	 * The list will get its elements dynamically from the elements request handler, which can be set using {@link #setDataHandler(String, String)}
	 * @param templateElement A template element for the dynamic elements of the list
	 */
	public WebinterfaceElementList(PageElement templateElement) {
		this.templateElement = templateElement;
	}

	private WebinterfaceElementList() {}

	public void setItems(ListAdapter<T> items) {
		this.items = items;
	}

	public ListAdapter<T> getItems() {
		return items;
	}

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

	public void setUpdateHandler(String requestTarget, String requestMethod) {
		this.updateRequestTarget = requestTarget;
		this.updateRequestMethod = requestMethod;
	}

	public String getUpdateRequestTarget() {
		return updateRequestTarget;
	}

	public String getUpdateRequestMethod() {
		return updateRequestMethod;
	}

	public boolean isDynamic() {
		return templateElement != null;
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

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout");
		el.appendAttribute("style", "grid-template-columns: 1fr;");

		if(templateElement != null) {
			if(!templateElement.isTemplate()) throw new IllegalStateException("Template element is not a template");
			el.addClass("dynamic-list");
			el.setAttribute("data-listItems", "[]");
//			el.setAttribute("data-updateRequestTarget", updateRequestTarget);
//			el.setAttribute("data-updateRequestMethod", updateRequestMethod);
			el.setAttribute("data-dataRequestTarget", dataRequestTarget);
			el.setAttribute("data-dataRequestMethod", dataRequestMethod);
			el.setAttribute("data-template", createDynamicListItem().toString());
		}else {
			for(T o : items.getItems()) {
//				el.appendChild(createListItem(o));
			}
		}

		return el;
	}

	private HtmlElement createListItem(T item) {
		String k = items.getIdentifier(item);
		HtmlElement container = new HtmlElement("div");
		container.addClass("list-item");
		container.appendChild(elementFunction.apply(item).toHtml());
		if(rearrangable) {
			if(updateRequestTarget == null || updateRequestMethod == null) throw new IllegalStateException("Update handler must be set if the list is rearrangable");
			HtmlButton upBtn = HtmlElement.button();
			upBtn.addClass("element-list-button");
			T itemBefore = items.getItemBefore(item);
			if(itemBefore != null) {
				ObjectValue moveUp = ActionValue.object();
				moveUp.put("action", ActionValue.string("swap"));
				moveUp.put("item1", ActionValue.string(k));
				moveUp.put("item2", ActionValue.string(items.getIdentifier(itemBefore)));
				upBtn.setOnClick(MultiAction.of(SendJSAction.of(updateRequestTarget, updateRequestMethod, moveUp).onSuccess(ReloadPageAction.reload())).createAttributeValue());
			}else {
				upBtn.setAttribute("disabled");
			}
			upBtn.appendChild(WebinterfaceUtils.iconifyIcon("mdi:chevron-up"));
			container.appendChild(upBtn);

			HtmlButton downBtn = HtmlElement.button();
			downBtn.addClass("element-list-button");
			T itemAfter = items.getItemAfter(item);
			if(itemAfter != null) {
				ObjectValue moveDown = ActionValue.object();
				moveDown.put("action", ActionValue.string("swap"));
				moveDown.put("item1", ActionValue.string(k));
				moveDown.put("item2", ActionValue.string(items.getIdentifier(itemAfter)));
				downBtn.setOnClick(MultiAction.of(SendJSAction.of(updateRequestTarget, updateRequestMethod, moveDown).onSuccess(ReloadPageAction.reload())).createAttributeValue());
			}else {
				downBtn.setAttribute("disabled");
			}
			downBtn.appendChild(WebinterfaceUtils.iconifyIcon("mdi:chevron-down"));
			container.appendChild(downBtn);
		}

		if(removable) {
			if(updateRequestTarget == null || updateRequestMethod == null) throw new IllegalStateException("Update handler must be set if the list is removable");
			HtmlButton removeBtn = HtmlElement.button();
			removeBtn.addClass("element-list-button");
			ObjectValue remove = ActionValue.object();
			remove.put("action", ActionValue.string("remove"));
			remove.put("item", ActionValue.string(k));
			removeBtn.setOnClick(MultiAction.of(SendJSAction.of(updateRequestTarget, updateRequestMethod, remove).onSuccess(ReloadPageAction.reload())).createAttributeValue());
			removeBtn.appendChild(WebinterfaceUtils.iconifyIcon("mdi:delete"));
			container.appendChild(removeBtn);
		}
		return container;
	}

	private HtmlElement createDynamicListItem() {
		HtmlElement container = new HtmlElement("div");
		container.addClass("list-item");
		container.appendChild(templateElement.toHtml());
		if(rearrangable) {
			if(updateRequestTarget == null || updateRequestMethod == null) throw new IllegalStateException("Update handler must be set if the list is rearrangable");
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
			if(updateRequestTarget == null || updateRequestMethod == null) throw new IllegalStateException("Update handler must be set if the list is removable");
			HtmlButton removeBtn = HtmlElement.button();
			removeBtn.addClass("list-button");
			removeBtn.setOnClick("dynamicListElementRemove(this.parentElement)");
			removeBtn.appendChild(WebinterfaceUtils.iconifyIcon("mdi:delete"));
			container.appendChild(removeBtn);
		}
		return container;
	}

	public static <T> ActionResponse handleUpdate(ActionEvent event, ListAdapter<T> adapter) {
		JSONObject value = event.getData();
		String action = value.getString("action");
		switch(action) {
			case "swap":
				adapter.swap(value.getString("item1"), value.getString("item2"));
				break;
			case "remove":
				adapter.remove(value.getString("item"));
		}
		return ActionResponse.success();
	}

	public static <T> ActionResponse handleData(ActionEvent event, ListAdapter<T> items, Function<T, JSONObject> objectFunction) {
		JSONArray elements = new JSONArray();
		for(T o : items.getItems()) {
			String k = items.getIdentifier(o);
			JSONObject obj = objectFunction.apply(o);

			String before = items.getIdentifier(items.getItemBefore(o));
			String after = items.getIdentifier(items.getItemAfter(o));

			obj.put("_id", k);
			obj.put("_before", before);
			obj.put("_after", after);
			elements.add(obj);
		}

		if(!elements.isEmpty()) {
			elements.getJSONObject(0).put("_first", true);
			elements.getJSONObject(elements.size() - 1).put("_last", true);
		}

		JSONObject obj = new JSONObject();
		obj.put("elements", elements);
		return ActionResponse.success(obj);
	}

	public static <T> Builder<T> builder() {
		return new Builder<>(new WebinterfaceElementList<>());
	}

	public static class Builder<T> extends AbstractElementBuilder<WebinterfaceElementList<T>, Builder<T>> {

		private Builder(WebinterfaceElementList<T> element) {
			super(element);
		}

		/**
		 * Sets the items used for a non-dynamic list.<br>
		 * An element function to convert the items to {@link PageElement}s must be set for a non-dynamic list using {@link #elementFunction(Function)}.<br>
		 * <br>
		 * Note: {@link #templateElement(PageElement)} and {@link #dataHandler(String, String)} must not be called after this point, as after this call the list is considered to be non-dynamic!
		 * @param items A {@link ListAdapter} to provide a list of items
		 * @return This builder
		 * @see #elementFunction(Function)
		 */
		public Builder<T> items(ListAdapter<T> items) {
			element.setItems(items);
			return this;
		}

		/**
		 * Sets the template element used for a dynamic list.<br>
		 * A data handler must be set for a dynamic list using {@link #dataHandler(String, String)}.<br>
		 * <br>
		 * Note: {@link #items(ListAdapter)} and {@link #elementFunction(Function)} must not be called after this point, as after this call the list is considered to be dynamic!
		 * @param templateElement The template element to use for the elements of this list
		 * @return This builder
		 * @see #dataHandler(String, String)
		 */
		public Builder<T> templateElement(PageElement templateElement) {
			element.setTemplateElement(templateElement);
			return this;
		}

		/**
		 * Changes whether items in this list should be rearrangable (using up and down buttons).<br>
		 * If <code>rearrangable</code> is set to <code>true</code>, you must set an update handler using {@link #updateHandler(String, String)}
		 * @param rearrangable Whether the items in this list should be rearrangable
		 * @return This builder
		 * @see #updateHandler(String, String)
		 */
		public Builder<T> rearrangable(boolean rearrangable) {
			element.setRearrangable(rearrangable);
			return this;
		}

		/**
		 * Changes whether itms in this list should be removable (using a delete button).<br>
		 * If <code>removable</code> is set to <code>true</code>, you must set an update handler using {@link #updateHandler(String, String)}
		 * @param removable Whether the items in this list should be removable
		 * @return This builder
		 */
		public Builder<T> removable(boolean removable) {
			element.setRemovable(removable);
			return this;
		}

		/**
		 * Sets an update handler for this list which will be called if an item is rearranged or removed.<br>
		 * It does not need to be set if {@link #rearrangable(boolean)} and {@link #removable(boolean)} are set to false (which they are by default)
		 * @param requestTarget The request target of the update handler
		 * @param requestMethod The request method of the update handler
		 * @return This builder
		 */
		public Builder<T> updateHandler(String requestTarget, String requestMethod) {
			element.setUpdateHandler(requestTarget, requestMethod);
			return this;
		}

		/**
		 * Sets a data handler for this list to retrieve data when it is updated (either by rearranging/removing an item or using an {@link UpdateElementAction}.<br>
		 * This must not be set if the list is non-dynamic (See {@link #items(ListAdapter)} for details)
		 * @param requestTarget The request target of the data handler
		 * @param requestMethod The request method of the data handler
		 * @return This builder
		 */
		public Builder<T> dataHandler(String requestTarget, String requestMethod) {
			element.setDataHandler(requestTarget, requestMethod);
			return this;
		}

		/**
		 * Sets a function to convert the items of this list to {@link PageElement}s.<br>
		 * This must not be set if the list is dynamic (See {@link #templateElement(PageElement)} for details)
		 * @param elementFunction The function to convert the items
		 * @return This builder
		 */
		public Builder<T> elementFunction(Function<T, PageElement> elementFunction) {
//			element.setElementFunction(elementFunction);
			return this;
		}

		@Override
		public WebinterfaceElementList<T> create() {
			if(element.getItems() == null && element.getTemplateElement() == null) throw new IllegalStateException("Either items or template element must be set");
			if(element.getItems() != null && element.getTemplateElement() != null) throw new IllegalStateException("Items and template element may not both be set");

			if(!element.isDynamic()) {
//				if(element.getElementFunction() == null) throw new IllegalStateException("Element function must be set if the list is not dynamic");
				if(element.getDataRequestTarget() != null || element.getDataRequestMethod() != null) throw new IllegalStateException("Data handler may not be set if the list is not dynamic");
			}else {
				if(element.getDataRequestTarget() == null || element.getDataRequestMethod() == null) throw new IllegalStateException("Data handler must be set if the list is dynamic");
//				if(element.getElementFunction() != null) throw new IllegalStateException("Element function may not be set if the list is dynamic");
			}

			if((element.isRearrangable() || element.isRemovable())
					&& (element.getUpdateRequestTarget() == null || element.getUpdateRequestMethod() == null)) throw new IllegalStateException("Update handler must be set if the list is rearrangable/removable");

			return super.create();
		}

	}

}
