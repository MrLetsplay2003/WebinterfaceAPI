package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import me.mrletsplay.webinterfaceapi.html.HtmlElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;

public class WebinterfaceSelect extends AbstractWebinterfacePageElement {
	
	private Supplier<List<Option>> options;
	private WebinterfaceAction onChangeAction;
	
	public WebinterfaceSelect() {
		this.options = ArrayList::new;
	}
	
	public void addOption(String name, String value, boolean selected) {
		Supplier<List<Option>> o = this.options;
		this.options = () -> {
			List<Option> ops = o.get();
			ops.add(new Option(name, value, selected));
			return ops;
		};
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
			b.appendChild(oe);
		}
		if(onChangeAction != null) b.setAttribute("onchange", onChangeAction.createAttributeValue());
		return b;
	}
	
	public static class Option {
		
		private String name;
		private String value;
		private boolean selected;
		
		public Option(String name, String value, boolean selected) {
			this.name = name;
			this.value = value;
			this.selected = selected;
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
		
	}

}
