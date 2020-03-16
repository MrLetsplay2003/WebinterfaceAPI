package me.mrletsplay.webinterfaceapi.css;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.css.selector.CssSelector;

public class CssElement {
	
	private CssSelector selector;
	private Map<String, Supplier<String>> properties;
	
	public CssElement(CssSelector selector) {
		this.selector = selector;
		this.properties = new HashMap<>();
	}
	
	public void setSelector(CssSelector selector) {
		this.selector = selector;
	}
	
	public CssSelector getSelector() {
		return selector;
	}
	
	public void setProperty(String name, Supplier<String> value) {
		properties.put(name, value);
	}
	
	public void setProperty(String name, String value) {
		setProperty(name, () -> value);
	}
	
	public Supplier<String> getProperty(String name) {
		return properties.get(name);
	}
	
	public Map<String, Supplier<String>> getProperties() {
		return properties;
	}
	
	public boolean isEmpty() {
		return properties.isEmpty();
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(selector.toString()).append("{");
		for(Map.Entry<String, Supplier<String>> prop : properties.entrySet()) {
			b.append(prop.getKey()).append(":").append(prop.getValue().get()).append(";");
		}
		b.append("}");
		return b.toString();
	}

}
