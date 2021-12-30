package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.util.WebinterfaceUtils;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;

public class WebinterfaceElementList extends AbstractWebinterfacePageElement {
	
	private Map<String, WebinterfacePageElement> elements;
	
	private boolean
		rearrangable,
		removable;
	
	private Function<String, WebinterfaceAction>
		moveUpAction,
		moveDownAction,
		removeAction;
	
	public WebinterfaceElementList() {
		this.elements = new LinkedHashMap<>();
	}
	
	/**
	 * Adds an element to the list.<br>
	 * It is not recommended to use list indexes as the {@code identifier}, as they can be unreliable, for example if a user has outdated content on their page due to another user or internal process changing the content after the page was requested
	 * @param identifier The identifier for this element. Must be unique, will be passed to the {@link Function} provided to {@link #setMoveUpAction(Function)}, {@link #setMoveDownAction(Function)} and {@link #setRemoveAction(Function)}, if enabled
	 * @param element The element to add
	 * @see #setMoveUpAction(Function)
	 * @see #setMoveDownAction(Function)
	 * @see #setRemoveAction(Function)
	 */
	public void addElement(String identifier, WebinterfacePageElement element) {
		elements.put(identifier, element);
	}
	
	public void setRearrangable(boolean rearrangable) {
		this.rearrangable = rearrangable;
	}
	
	public void setMoveUpAction(Function<String, WebinterfaceAction> moveUpAction) {
		this.moveUpAction = moveUpAction;
	}
	
	public void setMoveDownAction(Function<String, WebinterfaceAction> moveDownAction) {
		this.moveDownAction = moveDownAction;
	}
	
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}
	
	public void setRemoveAction(Function<String, WebinterfaceAction> removeAction) {
		this.removeAction = removeAction;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout");
		el.appendAttribute("style", "grid-template-columns: 1fr;");
		for(Map.Entry<String, WebinterfacePageElement> e : elements.entrySet()) {
			HtmlElement container = new HtmlElement("div");
			container.addClass("element-list-element");
			container.appendChild(e.getValue().toHtml());
			if(rearrangable) {
				if(moveUpAction == null || moveDownAction == null) throw new IllegalStateException("Both moveUpAction and the moveDownAction must be set if the list is rearrangable");
				HtmlButton upBtn = HtmlElement.button();
				upBtn.addClass("element-list-button");
				upBtn.setOnClick(moveUpAction.apply(e.getKey()).createAttributeValue());
				upBtn.appendChild(WebinterfaceUtils.iconElement("mdi:chevron-up"));
				container.appendChild(upBtn);
				
				HtmlButton downBtn = HtmlElement.button();
				downBtn.addClass("element-list-button");
				downBtn.setOnClick(moveDownAction.apply(e.getKey()).createAttributeValue());
				downBtn.appendChild(WebinterfaceUtils.iconElement("mdi:chevron-down"));
				container.appendChild(downBtn);
			}
			
			if(removable) {
				if(removeAction == null) throw new IllegalStateException("removeAction must be set if the list is rearrangable");
				HtmlButton removeBtn = HtmlElement.button();
				removeBtn.addClass("element-list-button");
				removeBtn.setOnClick(removeAction.apply(e.getKey()).createAttributeValue());
				removeBtn.appendChild(WebinterfaceUtils.iconElement("mdi:close"));
				container.appendChild(removeBtn);
			}
			
			el.appendChild(container);
		}
		return el;
	}
	
}
