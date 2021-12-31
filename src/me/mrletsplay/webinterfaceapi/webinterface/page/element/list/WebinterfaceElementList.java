package me.mrletsplay.webinterfaceapi.webinterface.page.element.list;

import java.util.function.Function;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
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
		requestTarget,
		requestMethod;
	
	/**
	 * Constructs a list element with the items from the specified {@link ListAdapter}.<br>
	 * This will <b>not</b> automatically handle updates. You still need to set an update handler using {@link #setUpdateHandler(String, String)}, if you want to allow rearranging/remove items
	 * @param items The items to add to the list
	 * @param elementFunction A function to convert the elements to a {@link WebinterfacePageElement}
	 */
	public WebinterfaceElementList(ListAdapter<T> items, Function<T, WebinterfacePageElement> elementFunction) {
		this.items = items;
		this.elementFunction = elementFunction;
	}
	
	public void setRearrangable(boolean rearrangable) {
		this.rearrangable = rearrangable;
	}
	
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}
	
	public void setUpdateHandler(String requestTarget, String requestMethod) {
		this.requestTarget = requestTarget;
		this.requestMethod = requestMethod;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout");
		el.appendAttribute("style", "grid-template-columns: 1fr;");
		for(T o : items.getItems()) {
			String k = items.getIdentifier(o);
			HtmlElement container = new HtmlElement("div");
			container.addClass("element-list-element");
			container.appendChild(elementFunction.apply(o).toHtml());
			if(rearrangable) {
				if(requestTarget == null || requestMethod == null) throw new IllegalStateException("Both requestTarget and requestMethod must be set if the list is rearrangable");
				HtmlButton upBtn = HtmlElement.button();
				upBtn.addClass("element-list-button");
				T itemBefore = items.getItemBefore(o);
				if(itemBefore != null) {
					ObjectValue moveUp = new ObjectValue();
					moveUp.put("action", new StringValue("swap"));
					moveUp.put("item1", new StringValue(k));
					moveUp.put("item2", new StringValue(items.getIdentifier(itemBefore)));
					upBtn.setOnClick(new SendJSAction(requestTarget, requestMethod, moveUp).createAttributeValue());
				}else {
					upBtn.setAttribute("disabled");
				}
				upBtn.appendChild(WebinterfaceUtils.iconElement("mdi:chevron-up"));
				container.appendChild(upBtn);
				
				HtmlButton downBtn = HtmlElement.button();
				downBtn.addClass("element-list-button");
				T itemAfter = items.getItemAfter(o);
				if(itemAfter != null) {
					ObjectValue moveDown = new ObjectValue();
					moveDown.put("action", new StringValue("swap"));
					moveDown.put("item1", new StringValue(k));
					moveDown.put("item2", new StringValue(items.getIdentifier(itemAfter)));
					downBtn.setOnClick(new SendJSAction(requestTarget, requestMethod, moveDown).createAttributeValue());
				}else {
					downBtn.setAttribute("disabled");
				}
				downBtn.appendChild(WebinterfaceUtils.iconElement("mdi:chevron-down"));
				container.appendChild(downBtn);
			}
			
			if(removable) {
				if(requestTarget == null || requestMethod == null) throw new IllegalStateException("Both requestTarget and requestMethod must be set if the list is removable");
				HtmlButton removeBtn = HtmlElement.button();
				removeBtn.addClass("element-list-button");
				ObjectValue remove = new ObjectValue();
				remove.put("action", new StringValue("remove"));
				remove.put("item", new StringValue(k));
				removeBtn.setOnClick(new SendJSAction(requestTarget, requestMethod, remove).createAttributeValue());
				removeBtn.appendChild(WebinterfaceUtils.iconElement("mdi:close"));
				container.appendChild(removeBtn);
			}
			
			el.appendChild(container);
		}
		return el;
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
		return null;
	}
	
}
