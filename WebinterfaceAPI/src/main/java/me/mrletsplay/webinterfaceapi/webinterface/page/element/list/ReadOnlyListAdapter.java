package me.mrletsplay.webinterfaceapi.webinterface.page.element.list;

import java.util.List;
import java.util.function.Function;

public class ReadOnlyListAdapter<T> extends BasicListAdapter<T> {
	
	public ReadOnlyListAdapter(List<T> items, Function<T, String> identifierFunction) {
		super(items, identifierFunction);
	}

	@Override
	public void swap(String identifier1, String identifier2) {
		throw new UnsupportedOperationException("List is read-only");
	}
	
	@Override
	public void remove(String identifier) {
		throw new UnsupportedOperationException("List is read-only");
	}

}
