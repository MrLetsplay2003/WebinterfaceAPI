package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;
import me.mrletsplay.webinterfaceapi.webinterface.config.WebinterfaceConfig;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAfterAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ArrayValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.CheckboxValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WrapperValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;

public class WebinterfaceSettingsPane extends WebinterfaceElementGroup {
	
	private Supplier<WebinterfaceConfig> config;
	private List<WebinterfaceSetting<?>> settings;
	private String
		requestTarget,
		requestMethod;
	
	public WebinterfaceSettingsPane(Supplier<WebinterfaceConfig> config, List<WebinterfaceSetting<?>> settings, String requestTarget, String requestMethod) {
		this.config = config;
		this.settings = new ArrayList<>();
		this.requestTarget = requestTarget;
		this.requestMethod = requestMethod;
		
		addLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
		addInnerLayoutProperties(new GridLayout("33fr", "66fr"));
		
		addSettings(settings);
	}
	
	public WebinterfaceSettingsPane(WebinterfaceConfig config, List<WebinterfaceSetting<?>> settings, String requestTarget, String requestMethod) {
		this(() -> config, settings, requestTarget, requestMethod);
	}
	
	public void addSettings(List<WebinterfaceSetting<?>> settings) {
		List<WebinterfaceSetting<?>> sL = new ArrayList<>(settings);
		Collections.sort(sL, Comparator.comparing(WebinterfaceSetting::getKey));
		sL.forEach(this::addSetting);
	}
	
	public void addSetting(WebinterfaceSetting<?> setting) {
		settings.add(setting);
		WebinterfaceText t = new WebinterfaceText(setting.getFriendlyName() != null ? setting.getFriendlyName() : setting.getKey());
		t.addLayoutProperties(DefaultLayoutProperty.CENTER_VERTICALLY);
		
		WebinterfacePageElement el = null;
		
		if(setting.getType().equals(Complex.value(String.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> {
				String v = (String) config.get().getSetting(setting);
				return v == null ? "(none)" : v;
			});
			in.setOnChangeAction(changeSettingAction(setting, new ElementValue(in)));
			el = in;
		}else if(setting.getType().equals(Complex.value(Integer.class)) || setting.getType().equals(Complex.value(Double.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> config.get().getSetting(setting).toString());
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), setting.getType().equals(Complex.value(Integer.class)) ? "parseInt(%s)" : "parseFloat(%s)")));
			el = in;
		}else if(setting.getType().equals(Complex.value(Boolean.class))) {
			WebinterfaceCheckBox in = new WebinterfaceCheckBox(() -> (Boolean) config.get().getSetting(setting));
			in.addLayoutProperties(DefaultLayoutProperty.CENTER_VERTICALLY);
			in.setOnChangeAction(changeSettingAction(setting, new CheckboxValue(in)));
			el = in;
		}else if(setting.getType().equals(Complex.list(String.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), "%s.split(\",\").map(x=>x.trim())")));
			el = in;
		}else if(setting.getType().equals(Complex.list(Integer.class)) || setting.getType().equals(Complex.list(Double.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), "%s.split(\",\").map(x=>x.trim()).map(" + (setting.getType().equals(Complex.list(Integer.class)) ? "x=>parseInt(x)" : "x=>parseFloat(x)") + ")")));
			el = in;
		}else if(setting.getType().equals(Complex.list(Boolean.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), "%s.split(\",\").map(x=>x.trim()).map(x=>x==\"true\")")));
			el = in;
		}
		
		if(el == null) return;
		el.addLayoutProperties(DefaultLayoutProperty.SECOND_TO_LAST_COLUMN);
		addElement(t);
		addElement(el);
	}
	
	private MultiAction changeSettingAction(WebinterfaceSetting<?> setting, WebinterfaceActionValue value) {
		return new MultiAction(new SendJSAction(requestTarget, requestMethod, new ArrayValue(
				new StringValue(setting.getKey()),
				value
			)),
			new ReloadPageAfterAction(100));
	}
	
	public List<WebinterfaceSetting<?>> getSettings() {
		return settings;
	}
	
	public static WebinterfaceResponse handleSetSettingRequest(WebinterfaceConfig config, WebinterfaceRequestEvent event) {
		JSONArray keyAndValue = event.getRequestData().getJSONArray("value");
		WebinterfaceSetting<?> set = config.getSetting(keyAndValue.getString(0));
		WebinterfaceSettingsPane.setSetting(config, set, keyAndValue.get(1));
		return WebinterfaceResponse.success();
	}
	
	private static <T> void setSetting(WebinterfaceConfig config, WebinterfaceSetting<T> setting, Object value) {
		config.setSetting(setting, setting.getType().cast(value, WebinterfaceSettingsPane::jsonCast).get());
	}
	
	private static <T> NullableOptional<T> jsonCast(Object o, Class<T> typeClass, Complex<?> exactClass) {
		if(o == null) return NullableOptional.of(null);
		if(typeClass.isInstance(o)) return NullableOptional.of(typeClass.cast(o));
		if(Number.class.isAssignableFrom(typeClass)) {
			if(!(o instanceof Number)) return NullableOptional.empty();
			Number n = (Number) o;
			if(typeClass.equals(Integer.class)) {
				return NullableOptional.of(typeClass.cast(n.intValue()));
			}else if(typeClass.equals(Double.class)) {
				return NullableOptional.of(typeClass.cast(n.doubleValue()));
			}
		}
		return NullableOptional.empty();
	}
	
}
