package me.mrletsplay.webinterfaceapi.webinterface.page.element.list;

import java.util.function.Function;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ObjectValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.AbstractWebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;

public class WebinterfaceElementList<T> extends AbstractWebinterfacePageElement {
	
	private ListAdapter<T> items;
	
	private Function<T, WebinterfacePageElement> elementFunction;
	
	private boolean
		rearrangable,
		removable;
	
	private String
		updateRequestTarget,
		updateRequestMethod;
	
	private WebinterfacePageElement templateElement;
	
	private String
		dataRequestTarget,
		dataRequestMethod;
	
	/**
	 * Constructs a list element with the items from the specified {@link ListAdapter}.<br>
	 * This will <b>not</b> automatically handle updates. You still need to set an update handler using {@link #setDataHandler(String, String)}, if you want to allow rearranging/remove items
	 * @param items The items to add to the list
	 * @param elementFunction A function to convert the elements to a {@link WebinterfacePageElement}
	 */
	public WebinterfaceElementList(ListAdapter<T> items, Function<T, WebinterfacePageElement> elementFunction) {
		this.items = items;
		this.elementFunction = elementFunction;
	}
	
	/**
	 * Constructs a list element with the items from the specified {@link ListAdapter}.<br>
	 * This will <b>not</b> automatically handle updates. You still need to set an update handler using {@link #setDataHandler(String, String)}, if you want to allow rearranging/remove items.<br>
	 * The list will get its elements dynamically from the elements request handler, which can be set using {@link #setDataHandler(String, String)}
	 * @param items The items to add to the list
	 * @param templateElement A template element for the dynamic elements of the list
	 */
	public WebinterfaceElementList(WebinterfacePageElement templateElement) {
		this.templateElement = templateElement;
	}
	
	public void setRearrangable(boolean rearrangable) {
		this.rearrangable = rearrangable;
	}
	
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}
	
	public void setUpdateHandler(String requestTarget, String requestMethod) {
		this.updateRequestTarget = requestTarget;
		this.updateRequestMethod = requestMethod;
	}
	
	public boolean isDynamic() {
		return templateElement != null;
	}
	
	public void setDataHandler(String requestTarget, String requestMethod) {
		this.dataRequestTarget = requestTarget;
		this.dataRequestMethod = requestMethod;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout");
		el.appendAttribute("style", "grid-template-columns: 1fr;");
		
		if(templateElement != null) {
			if(!templateElement.isTemplate()) throw new IllegalStateException("Template element is not a template");
			el.addClass("dynamic-list");
			el.setAttribute("data-updateRequestTarget", updateRequestTarget);
			el.setAttribute("data-updateRequestMethod", updateRequestMethod);
			el.setAttribute("data-dataRequestTarget", dataRequestTarget);
			el.setAttribute("data-dataRequestMethod", dataRequestMethod);
			el.setAttribute("data-template", createDynamicListItem().toString());
		}else {
			for(T o : items.getItems()) {
				el.appendChild(createListItem(o));
			}
		}
		
		return el;
	}
	
	private HtmlElement createListItem(T item) {
		String k = items.getIdentifier(item);
		HtmlElement container = new HtmlElement("div");
		container.addClass("element-list-element");
		container.appendChild(elementFunction.apply(item).toHtml());
		if(rearrangable) {
			if(updateRequestTarget == null || updateRequestMethod == null) throw new IllegalStateException("Both updateRequestTarget and updateRequestMethod must be set if the list is rearrangable");
			HtmlButton upBtn = HtmlElement.button();
			upBtn.addClass("element-list-button");
			T itemBefore = items.getItemBefore(item);
			if(itemBefore != null) {
				ObjectValue moveUp = new ObjectValue();
				moveUp.put("action", new StringValue("swap"));
				moveUp.put("item1", new StringValue(k));
				moveUp.put("item2", new StringValue(items.getIdentifier(itemBefore)));
				upBtn.setOnClick(MultiAction.of(new SendJSAction(updateRequestTarget, updateRequestMethod, moveUp).onSuccess(ReloadPageAction.reload())).createAttributeValue());
			}else {
				upBtn.setAttribute("disabled");
			}
			upBtn.appendChild(WebinterfaceUtils.iconElement("mdi:chevron-up"));
			container.appendChild(upBtn);
			
			HtmlButton downBtn = HtmlElement.button();
			downBtn.addClass("element-list-button");
			T itemAfter = items.getItemAfter(item);
			if(itemAfter != null) {
				ObjectValue moveDown = new ObjectValue();
				moveDown.put("action", new StringValue("swap"));
				moveDown.put("item1", new StringValue(k));
				moveDown.put("item2", new StringValue(items.getIdentifier(itemAfter)));
				downBtn.setOnClick(MultiAction.of(new SendJSAction(updateRequestTarget, updateRequestMethod, moveDown).onSuccess(ReloadPageAction.reload())).createAttributeValue());
			}else {
				downBtn.setAttribute("disabled");
			}
			downBtn.appendChild(WebinterfaceUtils.iconElement("mdi:chevron-down"));
			container.appendChild(downBtn);
		}
		
		if(removable) {
			if(updateRequestTarget == null || updateRequestMethod == null) throw new IllegalStateException("Both updateRequestTarget and updateRequestMethod must be set if the list is removable");
			HtmlButton removeBtn = HtmlElement.button();
			removeBtn.addClass("element-list-button");
			ObjectValue remove = new ObjectValue();
			remove.put("action", new StringValue("remove"));
			remove.put("item", new StringValue(k));
			removeBtn.setOnClick(MultiAction.of(new SendJSAction(updateRequestTarget, updateRequestMethod, remove).onSuccess(ReloadPageAction.reload())).createAttributeValue());
			removeBtn.appendChild(WebinterfaceUtils.iconElement("mdi:close"));
			container.appendChild(removeBtn);
		}
		return container;
	}
	
	private HtmlElement createDynamicListItem() {
		HtmlElement container = new HtmlElement("div");
		container.addClass("element-list-element");
		container.setAttribute("data-elementId", "${_id}");
		container.setAttribute("data-elementBefore", "${_before}");
		container.setAttribute("data-elementAfter", "${_after}");
		container.appendChild(templateElement.toHtml());
		if(rearrangable) {
			if(updateRequestTarget == null || updateRequestMethod == null) throw new IllegalStateException("Both updateRequestTarget and updateRequestMethod must be set if the list is rearrangable");
			HtmlButton upBtn = HtmlElement.button();
			upBtn.addClass("element-list-button");
			upBtn.setAttribute("${_first?disabled:}");
			upBtn.setOnClick("dynamicListElementUp(this)");
			upBtn.appendChild(WebinterfaceUtils.iconElement("mdi:chevron-up"));
			container.appendChild(upBtn);
			
			HtmlButton downBtn = HtmlElement.button();
			downBtn.addClass("element-list-button");
			downBtn.setAttribute("${_last?disabled:}");
			downBtn.setOnClick("dynamicListElementDown(this)");
			downBtn.appendChild(WebinterfaceUtils.iconElement("mdi:chevron-down"));
			container.appendChild(downBtn);
		}
		
		if(removable) {
			if(updateRequestTarget == null || updateRequestMethod == null) throw new IllegalStateException("Both updateRequestTarget and updateRequestMethod must be set if the list is removable");
			HtmlButton removeBtn = HtmlElement.button();
			removeBtn.addClass("element-list-button");
			removeBtn.setOnClick("dynamicListElementRemove(this)");
			removeBtn.appendChild(WebinterfaceUtils.iconElement("mdi:close"));
			container.appendChild(removeBtn);
		}
		return container;
	}
	
	public static <T> WebinterfaceResponse handleUpdate(WebinterfaceRequestEvent event, ListAdapter<T> adapter) {
		JSONObject value = event.getRequestData().getJSONObject("value");
		String action = value.getString("action");
		switch(action) {
			case "swap":
				adapter.swap(value.getString("item1"), value.getString("item2"));
				break;
			case "remove":
				adapter.remove(value.getString("item"));
		}
		return WebinterfaceResponse.success();
	}
	
	public static <T> WebinterfaceResponse handleData(WebinterfaceRequestEvent event, ListAdapter<T> items, Function<T, JSONObject> objectFunction) {
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
		return WebinterfaceResponse.success(obj);
	}
	
}
