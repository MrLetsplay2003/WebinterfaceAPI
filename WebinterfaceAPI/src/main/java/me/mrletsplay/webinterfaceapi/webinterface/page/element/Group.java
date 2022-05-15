package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.list.ListAdapter;

public class Group extends AbstractPageElement {

	private List<PageElement> elements;

	private PageElement templateElement;

	private String
		dataRequestTarget,
		dataRequestMethod;

	public Group() {
		this.elements = new ArrayList<>();
		addLayoutOptions(DefaultLayoutOption.NO_PADDING);
	}

	public Group(PageElement templateElement) {
		this();
		this.templateElement = templateElement;
	}

	public void addTitle(Supplier<String> title) {
		TitleText t = new TitleText(title);
		t.setText(title);
		t.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addElement(t);
	}

	public void addTitle(String title) {
		addTitle(() -> title);
	}

	public void addElement(PageElement element) {
		elements.add(element);
	}

	public List<PageElement> getElements() {
		return elements;
	}

	public void setTemplateElement(PageElement templateElement) {
		this.templateElement = templateElement;
	}

	public PageElement getTemplateElement() {
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
			for(PageElement e : elements) {
				el.appendChild(e.toHtml());
			}
		}

		return el;
	}

	public static <T> ActionResponse handleData(ActionEvent event, ListAdapter<T> items, Function<T, JSONObject> objectFunction) {
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
		return ActionResponse.success(obj);
	}

}
