package me.mrletsplay.webinterfaceapi.page.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicList<T> implements DynamicContent<List<T>>{
	
	private List<Object> content;
	
	public DynamicList() {
		this.content = new ArrayList<>();
	}
	
	public void addStatic(T t) {
		content.add(t);
	}
	
	public void addDynamic(DynamicContent<T> t) {
		content.add(t);
	}
	
	public void addDynamicMultiple(DynamicMultiple<T> ts) {
		content.add(ts);
	}
	
	public void addDynamicOptional(DynamicOptional<T> t) {
		content.add(t);
	}
	
	@Override
	public List<T> create() {
		return content.stream()
				.flatMap(o -> createObject(o).stream())
				.collect(Collectors.toList());
	}
	
	@SuppressWarnings("unchecked")
	private List<T> createObject(Object o) {
		if(o instanceof DynamicOptional<?>) return ((DynamicOptional<T>) o).create().map(Collections::singletonList).orElse(Collections.emptyList());
		if(o instanceof DynamicMultiple<?>) return ((DynamicMultiple<T>) o).create();
		if(o instanceof DynamicContent<?>) return Collections.singletonList(((DynamicContent<T>) o).create());
		return Collections.singletonList((T) o);
	}
	
	@Override
	public boolean isStatic() {
		return content.stream()
				.allMatch(c -> !(c instanceof DynamicContent<?>));
	}

}
