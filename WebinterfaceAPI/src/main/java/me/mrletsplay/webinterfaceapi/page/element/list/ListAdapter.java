package me.mrletsplay.webinterfaceapi.page.element.list;

import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;

/**
 * Represents a list-type structure which can be modified from the webinterface by swapping and removing items.<br>
 * An implementation for a normal {@link List} is {@link BasicListAdapter}
 * @author MrLetsplay2003
 *
 * @param <T> The type of the elements
 */
public interface ListAdapter<T> {

	public JSONObject toJSON(T item);

	public T fromJSON(JSONObject json);

	public List<T> getItems();

	public default List<T> getItems(JSONArray array) {
		return array.stream()
			.map(o -> fromJSON((JSONObject) o))
			.collect(Collectors.toList());
	}

}