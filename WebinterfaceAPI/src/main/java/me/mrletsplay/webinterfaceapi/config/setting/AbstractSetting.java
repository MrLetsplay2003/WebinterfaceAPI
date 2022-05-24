package me.mrletsplay.webinterfaceapi.config.setting;

import java.util.List;

import me.mrletsplay.mrcore.misc.Complex;

public abstract class AbstractSetting<Self extends Setting<T>, T> implements Setting<T> {

	private SettingsCategory category;
	private String key;
	private T defaultValue;
	private List<T> allowedValues;
	private Complex<T> type;
	private String friendlyName;
	private String description;

	public AbstractSetting(SettingsCategory category, String key, T defaultValue, Complex<T> type) {
		this.category = category;
		this.key = key;
		this.defaultValue = defaultValue;
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	private Self getSelf() {
		return (Self) this;
	}

	@Override
	public String getKey() {
		return key;
	}

	public Self defaultValue(T value) {
		this.defaultValue = value;
		return getSelf();
	}

	@Override
	public T getDefaultValue() {
		return defaultValue;
	}

	public Self allowedValues(List<T> values) {
		this.allowedValues = values;
		return getSelf();
	}

	@Override
	public List<T> getAllowedValues() {
		return allowedValues;
	}

	@Override
	public Complex<T> getType() {
		return type;
	}

	public Self friendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
		return getSelf();
	}

	@Override
	public String getFriendlyName() {
		return friendlyName;
	}

	public Self description(String description) {
		this.description = description;
		return getSelf();
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public SettingsCategory getCategory() {
		return category;
	}

}
