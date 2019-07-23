package me.mrletsplay.webinterfaceapi.html;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.text.StringEscapeUtils;

import me.mrletsplay.webinterfaceapi.css.StyleSheet;
import me.mrletsplay.webinterfaceapi.html.element.HtmlBr;
import me.mrletsplay.webinterfaceapi.html.element.HtmlButton;
import me.mrletsplay.webinterfaceapi.html.element.HtmlLink;
import me.mrletsplay.webinterfaceapi.html.element.HtmlOption;
import me.mrletsplay.webinterfaceapi.html.element.HtmlScript;
import me.mrletsplay.webinterfaceapi.html.element.HtmlSelect;
import me.mrletsplay.webinterfaceapi.js.JavaScriptScript;

public class HtmlElement {

	private HtmlElement parent;
	private String type;
	private Supplier<String> text;
	private Map<String, Supplier<String>> attributes;
	private List<HtmlElement> children;
	private boolean selfClosing;
	protected EnumSet<HtmlElementFlag> flags;
	
	public HtmlElement(String type) {
		this.type = type;
		this.attributes = new HashMap<>();
		this.children = new ArrayList<>();
		this.flags = EnumSet.noneOf(HtmlElementFlag.class);
	}
	
	public HtmlElement getParent() {
		return parent;
	}
	
	public String getType() {
		return type;
	}
	
	public void setText(Supplier<String> text) {
		this.text = text;
	}
	
	public void setText(String text) {
		setText(() -> text);
	}
	
	public Supplier<String> getText() {
		return text;
	}
	
	public void setAttribute(String name, Supplier<String> value) {
		attributes.put(name, value);
	}
	
	public void setAttribute(String name, String value) {
		setAttribute(name, () -> value);
	}
	
	public void setAttribute(String name) {
		setAttribute(name, (String) null);
	}
	
	public void unsetAttribute(String name) {
		attributes.remove(name);
	}
	
	public Supplier<String> getAttribute(String name) {
		return attributes.get(name);
	}
	
	public Map<String, Supplier<String>> getAttributes() {
		return attributes;
	}
	
	public void appendChild(HtmlElement child) {
		if(child.getParent() != null) child.getParent().removeChild(child);
		HtmlElement tmpC = this;
		while(tmpC.getParent() != null) {
			tmpC = tmpC.getParent();
			if(tmpC == this) throw new HtmlException("Impossible circular hierarchy");
		}
		children.add(child);
		child.parent = this;
	}
	
	public void removeChild(HtmlElement child) {
		children.remove(child);
	}
	
	public List<HtmlElement> getChildren() {
		return children;
	}
	
	public void setSelfClosing(boolean selfClosing) {
		this.selfClosing = selfClosing;
	}
	
	public boolean isSelfClosing() {
		return selfClosing;
	}
	
	public void setID(Supplier<String> id) {
		setAttribute("id", id);
	}
	
	public void setID(String id) {
		setAttribute("id", id);
	}
	
	public Supplier<String> getID() {
		return getAttribute("id");
	}
	
	public HtmlElement copy() {
		return copy(false);
	}
	
	public HtmlElement deepCopy() {
		return copy(true);
	}
	
	protected HtmlElement copy(boolean deep) {
		HtmlElement el = new HtmlElement(type);
		applyAttributes(el, deep);
		return el;
	}
	
	protected void applyAttributes(HtmlElement copy, boolean deep) {
		copy.type = type;
		copy.text = text;
		copy.attributes = new HashMap<>(attributes);
		if(deep) {
			for(HtmlElement child : children) {
				copy.appendChild(child.deepCopy());
			}
		}
		copy.selfClosing = selfClosing;
		copy.flags = EnumSet.copyOf(flags);
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("<").append(getType());
		for(Map.Entry<String, Supplier<String>> attr : getAttributes().entrySet()) {
			b.append(" ").append(attr.getKey());
			String attrV = attr.getValue().get();
			if(attrV != null) b.append("=\"").append(StringEscapeUtils.escapeHtml4(attrV)).append("\"");
		}
		if(isSelfClosing()) {
			b.append("/>");
			return b.toString(); // Self closing elements can't have content
		}
		b.append(">");
		if(getText() != null) b.append(flags.contains(HtmlElementFlag.DONT_ESCAPE_TEXT) ? getText().get() : StringEscapeUtils.escapeHtml4(getText().get()).replace("\n", "<br/>"));
		for(HtmlElement child : getChildren()) {
			b.append(child.toString());
		}
		b.append("</").append(getType()).append(">");
		return b.toString();
	}
	
	public static HtmlElement style(StyleSheet style) {
		HtmlElement st = new HtmlElement("style");
		st.setText(() -> style.toString());
		return st;
	}
	
	public static HtmlElement script(JavaScriptScript script) {
		HtmlElement st = script();
		st.setText(() -> script.toString());
		return st;
	}
	
	public static HtmlSelect select() {
		return new HtmlSelect();
	}
	
	public static HtmlOption option() {
		return new HtmlOption();
	}
	
	public static HtmlLink link() {
		return new HtmlLink();
	}
	
	public static HtmlBr br() {
		return new HtmlBr();
	}
	
	public static HtmlScript script() {
		return new HtmlScript();
	}
	
	public static HtmlElement p() {
		return new HtmlElement("p");
	}
	
	public static HtmlButton button() {
		return new HtmlButton();
	}
	
}
