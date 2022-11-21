package me.mrletsplay.webinterfaceapi.page.action.value;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.text.StringEscapeUtils;

import me.mrletsplay.webinterfaceapi.page.element.CheckBox;
import me.mrletsplay.webinterfaceapi.page.element.ElementID;
import me.mrletsplay.webinterfaceapi.page.element.PageElement;
import me.mrletsplay.webinterfaceapi.page.element.list.ElementList;

public interface ActionValue {

	public String toJavaScript();

	public static ActionValue nullValue() {
		return () -> "null";
	}

	public default ActionValue plus(ActionValue other) {
		return () -> toJavaScript() + " + " + other.toJavaScript();
	}

	public default ActionValue asInt() {
		return () -> "parseInt(" + toJavaScript() + ")";
	}

	public default ActionValue key(String key) {
		return () -> toJavaScript() + "[" + StringEscapeUtils.escapeEcmaScript(key) + "]";
	}

	public default ActionValue index(int index) {
		return key(String.valueOf(index));
	}

	public default ActionValue urlEncoded() {
		return () -> "encodeURIComponent(" + toJavaScript() + ")";
	}

	public static ActionValue string(Supplier<String> string) {
		return () -> {
			String str = string.get();
			return str == null ? "null" : "\"" + StringEscapeUtils.escapeEcmaScript(str) + "\"";
		};
	}

	public static ActionValue string(String string) {
		return string(() -> string);
	}

	public static ActionValue integer(Supplier<Integer> integer) {
		return () -> String.valueOf(integer.get());
	}

	public static ActionValue integer(int integer) {
		return integer(() -> integer);
	}

	public static ActionValue decimal(Supplier<Double> decimal) {
		return () -> String.valueOf(decimal.get());
	}

	public static ActionValue decimal(double decimal) {
		return decimal(() -> decimal);
	}

	public static ActionValue bool(Supplier<Boolean> bool) {
		return () -> String.valueOf(bool.get());
	}

	public static ActionValue bool(boolean bool) {
		return bool(() -> bool);
	}

	public static ArrayValue array(List<ActionValue> values) {
		return new ArrayValue(values);
	}

	public static ArrayValue array(ActionValue... values) {
		return new ArrayValue(values);
	}

	public static ObjectValue object(Map<String, ActionValue> values) {
		return new ObjectValue(values);
	}

	public static ObjectValue object() {
		return new ObjectValue();
	}

	public static ActionValue elementValue(ElementID elementID) {
		elementID.require();
		return () -> "document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(elementID.get()) + "\").value";
	}

	public static ActionValue elementValue(PageElement element) {
		return elementValue(element.getID());
	}

	public static ActionValue checkboxValue(ElementID elementID) {
		elementID.require();
		return () -> "document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(elementID.get()) + "\").firstChild.checked";
	}

	public static ActionValue checkboxValue(CheckBox checkbox) {
		return checkboxValue(checkbox.getID());
	}

	public static ActionValue listItems(ElementID elementID) {
		elementID.require();
		return () -> "listGetItemsByID(\"" + StringEscapeUtils.escapeEcmaScript(elementID.get()) + "\")";
	}

	public static ActionValue listItems(ElementList<?> list) {
		return listItems(list.getID());
	}

	public static ActionValue elementAttribute(ElementID elementID, String attributeName) {
		elementID.require();
		return () -> "WebinterfaceUtils.getElementAttributeById(\"" + StringEscapeUtils.escapeEcmaScript(elementID.get()) + "\",\"" + StringEscapeUtils.escapeEcmaScript(attributeName) + "\")";
	}

	public static ActionValue elementAttribute(PageElement element, String attributeName) {
		return elementAttribute(element.getID(), attributeName);
	}

}
