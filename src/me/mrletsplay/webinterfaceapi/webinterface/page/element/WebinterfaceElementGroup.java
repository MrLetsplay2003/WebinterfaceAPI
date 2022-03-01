package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.list.ListAdapter;

public class WebinterfaceElementGroup extends AbstractWebinterfacePageElement {

	private List<WebinterfacePageElement> elements;
	
	private WebinterfacePageElement templateElement;
	
	private String
		dataRequestTarget,
		dataRequestMethod;
	
	public WebinterfaceElementGroup() {
		this.elements = new ArrayList<>();
	}
	
	public WebinterfaceElementGroup(WebinterfacePageElement templateElement) {
		this();
		this.templateElement = templateElement;
	}
	
	public void addTitle(Supplier<String> title) {
		WebinterfaceTitleText t = new WebinterfaceTitleText(title);
		t.setText(title);
		t.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addElement(t);
	}
	
	public void addTitle(String title) {
		addTitle(() -> title);
	}
	
	public void addElement(WebinterfacePageElement element) {
		elements.add(element);
	}
	
	public List<WebinterfacePageElement> getElements() {
		return elements;
	}
	
	public void setTemplateElement(WebinterfacePageElement templateElement) {
		this.templateElement = templateElement;
	}
	
	public WebinterfacePageElement getTemplateElement() {
		return templateElement;
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
	
	public boolean isDynamic() {
		return templateElement != null;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement el = new HtmlElement("div");
		el.addClass("grid-layout");
		
		if(templateElement != null) {
			if(!templateElement.isTemplate()) throw new IllegalStateException("Template element is not a template");
			el.addClass("dynamic-group");
			el.setAttribute("data-dataRequestTarget", dataRequestTarget);
			el.setAttribute("data-dataRequestMethod", dataRequestMethod);
			el.setAttribute("data-template", templateElement.toHtml().toString());
		}else {
			for(WebinterfacePageElement e : elements) {
				el.appendChild(e.toHtml());
			}
		}
		
		return el;
	}
	
	/**
	 * @deprecated Use {@link #handleData(WebinterfaceRequestEvent, ListAdapter, Function)} instead
	 * @param <T>
	 * @param event
	 * @param items
	 * @param objectFunction
	 * @return
	 */
	@Deprecated
	public static <T> WebinterfaceResponse handleData(WebinterfaceRequestEvent event, List<T> items, Function<T, JSONObject> objectFunction) {
		JSONArray elements = new JSONArray();
		for(T o : items) {
			JSONObject obj = objectFunction.apply(o);
			elements.add(obj);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("elements", elements);
		return WebinterfaceResponse.success(obj);
	}
	
	public static <T> WebinterfaceResponse handleData(WebinterfaceRequestEvent event, ListAdapter<T> items, Function<T, JSONObject> objectFunction) {
		JSONArray elements = new JSONArray();
		for(T o : items.getItems()) {
			JSONObject obj = objectFunction.apply(o);
			obj.put("_id", items.getIdentifier(o));
			T before = items.getItemBefore(o);
			if(before != null) obj.put("_before", items.getIdentifier(before));
			T after = items.getItemAfter(o);
			if(after != null) obj.put("_after", items.getIdentifier(after));
			elements.add(obj);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("elements", elements);
		return WebinterfaceResponse.success(obj);
	}

}
