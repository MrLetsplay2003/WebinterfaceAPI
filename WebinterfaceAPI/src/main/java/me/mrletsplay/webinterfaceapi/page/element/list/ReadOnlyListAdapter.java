package me.mrletsplay.webinterfaceapi.page.element.list;

import java.util.List;
import java.util.function.Function;

import me.mrletsplay.mrcore.json.JSONObject;

public class ReadOnlyListAdapter<T> extends BasicListAdapter<T> {

	public ReadOnlyListAdapter(List<T> items, Function<? super T, JSONObject> toJSON,
		Function<JSONObject, ? extends T> fromJSON) {
		super(items, toJSON, fromJSON);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onListUpdate(List<T> oldList, List<T> newList) {
		throw new UnsupportedOperationException();
	}

}
