package me.mrletsplay.webinterfaceapi.page.element.list;

import java.util.List;
import java.util.function.Function;

import me.mrletsplay.mrcore.json.JSONObject;

public class BasicListAdapter<T> implements ListAdapter<T> {

	private List<T> items;
	private Function<? super T, JSONObject> toJSON;
	private Function<JSONObject, ? extends T> fromJSON;

	/**
	 * Constructs a list adapter using the specified list.<br>
	 * The list may not contain null values or duplicate values!
	 * @param items
	 * @param identifierFunction
	 */
	public BasicListAdapter(List<T> items, Function<? super T, JSONObject> toJSON, Function<JSONObject, ? extends T> fromJSON) {
		this.items = items;
		this.toJSON = toJSON;
		this.fromJSON = fromJSON;
	}

	@Override
	public List<T> getItems() {
		return items;
	}

	@Override
	public JSONObject toJSON(T item) {
		return toJSON.apply(item);
	}

	@Override
	public T fromJSON(JSONObject json) {
		return fromJSON.apply(json);
	}

	@Override
	public void onListUpdate(List<T> oldList, List<T> newList) {
		this.items.clear();
		this.items.addAll(newList);
	}

}
