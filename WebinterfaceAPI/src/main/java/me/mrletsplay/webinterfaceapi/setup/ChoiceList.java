package me.mrletsplay.webinterfaceapi.setup;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChoiceList {

	private Map<String, String> choices;

	public ChoiceList() {
		this.choices = new LinkedHashMap<>();
	}

	public ChoiceList addChoice(String id, String name) {
		this.choices.put(id, name);
		return this;
	}

	public Map<String, String> getChoices() {
		return choices;
	}

}
