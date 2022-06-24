package me.mrletsplay.webinterfaceapi.setup;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;

public abstract class SetupStep {

	private String
		id,
		name,
		description;

	private List<SetupElement> elements;

	public SetupStep(String id, String name) {
		this.id = id;
		this.name = name;
		this.elements = new ArrayList<>();
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	private void addElement(SetupElement setting) {
		elements.add(setting);
	}

	public void addString(String id, String name, String initialValue) {
		addElement(new SetupElement(id, name, SetupElementType.STRING, initialValue));
	}

	public void addPassword(String id, String name, String initialValue) {
		addElement(new SetupElement(id, name, SetupElementType.PASSWORD, initialValue));
	}

	public void addInteger(String id, String name, Integer initialValue) {
		addElement(new SetupElement(id, name, SetupElementType.INTEGER, initialValue));
	}

	public void addDouble(String id, String name, Double initialValue) {
		addElement(new SetupElement(id, name, SetupElementType.DOUBLE, initialValue));
	}

	public void addBoolean(String id, String name, Boolean initialValue) {
		addElement(new SetupElement(id, name, SetupElementType.BOOLEAN, initialValue));
	}

	public void addHeading(String heading) {
		addElement(new SetupElement(null, heading, SetupElementType.HEADING, null));
	}

	public List<SetupElement> getElements() {
		return elements;
	}

	public abstract String callback(JSONObject data);

}
