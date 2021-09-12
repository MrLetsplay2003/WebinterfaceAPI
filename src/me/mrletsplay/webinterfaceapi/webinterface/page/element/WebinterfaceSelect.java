package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.builder.AbstractElementBuilder;

public class WebinterfaceSelect extends AbstractWebinterfacePageElement {
	
	private Supplier<List<Option>> options;
	private WebinterfaceAction onChangeAction;
	
	public WebinterfaceSelect() {
		this.options = ArrayList::new;
	}
	
	public void addOption(String name, String value, boolean selected, boolean enabled) {
		Supplier<List<Option>> o = this.options;
		this.options = () -> {
			List<Option> ops = o.get();
			ops.add(new Option(name, value, selected, enabled));
			return ops;
		};
	}
	
	public void addOption(String name, String value, boolean selected) {
		addOption(name, value, selected, true);
	}
	
	public void addOption(String name, String value) {
		addOption(name, value, false);
	}
	
	public void setOnChangeAction(WebinterfaceAction onChangeAction) {
		this.onChangeAction = onChangeAction;
	}
	
	@Override
	public HtmlElement createElement() {
		HtmlElement b = new HtmlElement("select");
		for(Option op : options.get()) {
			HtmlElement oe = new HtmlElement("option");
			oe.setText(op.getName());
			oe.setAttribute("value", op.getValue());
			if(op.isSelected()) oe.setAttribute("selected");
			if(!op.isEnabled()) oe.setAttribute("disabled");
			b.appendChild(oe);
		}
		if(onChangeAction != null) b.setAttribute("onchange", onChangeAction.createAttributeValue());
		return b;
	}
	
	public static class Option {
		
		private String name;
		private String value;
		private boolean selected;
		private boolean enabled;
		
		public Option(String name, String value, boolean selected, boolean enabled) {
			this.name = name;
			this.value = value;
			this.selected = selected;
			this.enabled = enabled;
		}
		
		public String getName() {
			return name;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		public boolean isSelected() {
			return selected;
		}
		
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		
		public boolean isEnabled() {
			return enabled;
		}
		
	}
	
	public static Builder builder() {
		return new Builder(new WebinterfaceSelect());
	}
	
	public static class Builder extends AbstractElementBuilder<WebinterfaceSelect, Builder> {

		private Builder(WebinterfaceSelect element) {
			super(element);
		}
		
		public Builder addOption(String name, String value, boolean selected, boolean enabled) {
			element.addOption(name, value, selected, enabled);
			return this;
		}
		
		public Builder addOption(String name, String value, boolean selected) {
			element.addOption(name, value, selected);
			return this;
		}
		
		public Builder addOption(String name, String value) {
			element.addOption(name, value);
			return this;
		}
		
		public Builder onChange(WebinterfaceAction onChange) {
			element.setOnChangeAction(onChange);
			return this;
		}
		
		public Builder onChange(Function<WebinterfaceSelect, WebinterfaceAction> onChange) {
			element.setOnChangeAction(onChange.apply(element));
			return this;
		}
		
		@Override
		public WebinterfaceSelect create() throws IllegalStateException {
			return super.create();
		}
		
	}

}
