package me.mrletsplay.webinterfaceapi.page.action.value;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.text.StringEscapeUtils;

import me.mrletsplay.webinterfaceapi.page.element.CheckBox;
import me.mrletsplay.webinterfaceapi.page.element.PageElement;

public interface ActionValue {

	public String toJavaScript();

	public default ActionValue plus(ActionValue other) {
		return () -> toJavaScript() + " + " + other.toJavaScript();
	}

	public default ActionValue asInt() {
		return () -> "parseInt(" + toJavaScript() + ")";
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

	public static ActionValue elementValue(String elementID) {
		return () -> "document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(elementID) + "\").value";
	}

	public static ActionValue elementValue(PageElement element) {
		return () -> "document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(element.getOrGenerateID()) + "\").value";
	}

	public static ActionValue checkboxValue(String elementID) {
		return () -> "document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(elementID) + "\").firstChild.checked";
	}

	public static ActionValue checkboxValue(CheckBox checkbox) {
		return () -> "document.getElementById(\"" + StringEscapeUtils.escapeEcmaScript(checkbox.getOrGenerateID()) + "\").firstChild.checked";
	}

	public static ActionValue elementAttribute(String elementID, String attributeName) {
		return () -> "WebinterfaceUtils.getElementAttributeById(\"" + StringEscapeUtils.escapeEcmaScript(elementID) + "\",\"" + StringEscapeUtils.escapeEcmaScript(attributeName) + "\")";
	}

	public static ActionValue elementAttribute(PageElement element, String attributeName) {
		return () -> "WebinterfaceUtils.getElementAttributeById(\"" + StringEscapeUtils.escapeEcmaScript(element.getOrGenerateID()) + "\",\"" + StringEscapeUtils.escapeEcmaScript(attributeName) + "\")";
	}

}
