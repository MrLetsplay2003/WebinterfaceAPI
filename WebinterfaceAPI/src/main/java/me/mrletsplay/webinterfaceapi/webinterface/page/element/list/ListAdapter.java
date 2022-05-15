package me.mrletsplay.webinterfaceapi.webinterface.page.element.list;

import java.util.List;

/**
 * Represents a list-type structure which can be modified from the webinterface by swapping and removing items.<br>
 * An implementation for a normal {@link List} is {@link BasicListAdapter}, a read-only version is {@link ReadOnlyListAdapter}
 * @author MrLetsplay2003
 *
 * @param <T> The type of the elements
 */
public interface ListAdapter<T> {
	
	public String getIdentifier(T t);
	
	public List<T> getItems();
	
	public T getItemBefore(T t);
	
	public T getItemAfter(T t);
	
	public void swap(String identifier1, String identifier2);
	
	public void remove(String identifier);
	
}