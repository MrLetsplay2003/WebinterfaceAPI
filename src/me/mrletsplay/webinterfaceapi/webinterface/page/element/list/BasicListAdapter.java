package me.mrletsplay.webinterfaceapi.webinterface.page.element.list;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BasicListAdapter<T> implements ListAdapter<T> {
	
	private List<T> items;
	private Function<T, String> identifierFunction;
	
	/**
	 * Constructs a list adapter using the specified list.<br>
	 * The list may not contain null values or duplicate values!
	 * @param items
	 * @param identifierFunction
	 */
	public BasicListAdapter(List<T> items, Function<T, String> identifierFunction) {
		this.items = items;
		this.identifierFunction = identifierFunction;
	}

	@Override
	public String getIdentifier(T t) {
		return identifierFunction.apply(t);
	}
	
	@Override
	public List<T> getItems() {
		return items;
	}
	
	@Override
	public T getItemBefore(T t) {
		int idx = items.indexOf(t);
		if(idx <= 0) return null;
		return items.get(idx - 1);
	}
	
	@Override
	public T getItemAfter(T t) {
		int idx = items.indexOf(t);
		if(idx < 0 || idx >= items.size() - 1) return null;
		return items.get(idx + 1);
	}

	@Override
	public void swap(String identifier1, String identifier2) {
		T i1 = getItem(identifier1);
		T i2 = getItem(identifier2);
		Collections.swap(items, items.indexOf(i1), items.indexOf(i2));
	}

	@Override
	public void remove(String identifier) {
		items.removeIf(i -> identifierFunction.apply(i).equals(identifier));
	}
	
	protected T getItem(String identifier) {
		return items.stream()
				.filter(i -> identifierFunction.apply(i).equals(identifier))
				.findFirst().orElse(null);
	}
	
}
