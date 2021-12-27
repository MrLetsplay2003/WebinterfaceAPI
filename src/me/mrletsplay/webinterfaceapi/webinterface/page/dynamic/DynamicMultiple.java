package me.mrletsplay.webinterfaceapi.webinterface.page.dynamic;

import java.util.ArrayList;
import java.util.List;

public interface DynamicMultiple<T> extends DynamicContent<List<T>> {
	
	public void create(List<T> sections);
	
	@Override
	public default List<T> create() {
		List<T> content = new ArrayList<>();
		create(content);
		return content;
	}

}
