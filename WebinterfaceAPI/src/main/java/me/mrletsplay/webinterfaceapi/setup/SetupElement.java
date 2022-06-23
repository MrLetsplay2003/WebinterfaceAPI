package me.mrletsplay.webinterfaceapi.setup;

public class SetupElement {

	private String
		id,
		name;

	private SetupElementType type;

	private Object initialValue;

	public SetupElement(String id, String name, SetupElementType type, Object initialValue) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.initialValue = initialValue;
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public SetupElementType getType() {
		return type;
	}

	public Object getInitialValue() {
		return initialValue;
	}

}
