package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;
import me.mrletsplay.webinterfaceapi.webinterface.config.WebinterfaceConfig;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfaceSettingsPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ArrayValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.CheckboxValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.RawValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WrapperValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;

public class WebinterfaceSettingsPane extends WebinterfaceElementGroup {
	
	private Supplier<WebinterfaceConfig> config;
	private List<SettingsCategory> categories;
	private String
		requestTarget,
		requestMethod;
	
	public WebinterfaceSettingsPane(Supplier<WebinterfaceConfig> config, List<SettingsCategory> settings, String requestTarget, String requestMethod) {
		this.config = config;
		this.categories = new ArrayList<>();
		this.requestTarget = requestTarget;
		this.requestMethod = requestMethod;
		
		addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		getStyle().setProperty("grid-template-columns", "1fr");
		
		addSettings(settings);
	}
	
	public WebinterfaceSettingsPane(WebinterfaceConfig config, List<SettingsCategory> settings, String requestTarget, String requestMethod) {
		this(() -> config, settings, requestTarget, requestMethod);
	}
	
	public void addSettings(List<SettingsCategory> categories) {
		categories.forEach(c -> {
			WebinterfaceHeading h = new WebinterfaceHeading(c.getName());
			h.setLevel(3);
			h.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
			h.getStyle().setProperty("margin", "0");
			addElement(h);
			c.getSettings().forEach(this::addSetting);
			addElement(new WebinterfaceVerticalSpacer("30px"));
		});
	}
	
	private void addSetting(WebinterfaceSetting<?> setting) {
		WebinterfacePageElement el = null;
		WebinterfaceActionValue defaultValue = null;
		
		boolean oneLineLayout = false;
		
		if(setting.getType().equals(Complex.value(String.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> {
				String v = (String) config.get().getSetting(setting);
				return v == null ? "(none)" : v;
			});
			in.setOnChangeAction(changeSettingAction(setting, new ElementValue(in)));
			el = in;
			defaultValue = new StringValue((String) setting.getDefaultValue());
		}else if(setting.getType().equals(Complex.value(Integer.class)) || setting.getType().equals(Complex.value(Double.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> config.get().getSetting(setting).toString());
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), setting.getType().equals(Complex.value(Integer.class)) ? "parseInt(%s)" : "parseFloat(%s)")));
			el = in;
			defaultValue = new RawValue(setting.getDefaultValue().toString());
		}else if(setting.getType().equals(Complex.value(Boolean.class))) {
			WebinterfaceCheckBox in = new WebinterfaceCheckBox(() -> (Boolean) config.get().getSetting(setting));
			in.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
			in.setOnChangeAction(changeSettingAction(setting, new CheckboxValue(in)));
			el = in;
			defaultValue = new RawValue(setting.getDefaultValue().toString());
			oneLineLayout = true;
		}else if(setting.getType().equals(Complex.list(String.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), "%s.split(\",\").map(x=>x.trim())")));
			el = in;
			defaultValue = new ArrayValue(Complex.list(String.class).cast(setting.getDefaultValue()).get().stream()
					.map(s -> new StringValue(s))
					.collect(Collectors.toList()));
		}else if(setting.getType().equals(Complex.list(Integer.class)) || setting.getType().equals(Complex.list(Double.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), "%s.split(\",\").map(x=>x.trim()).map(" + (setting.getType().equals(Complex.list(Integer.class)) ? "x=>parseInt(x)" : "x=>parseFloat(x)") + ")")));
			el = in;
			defaultValue = new ArrayValue(((List<?>) setting.getDefaultValue()).stream()
					.map(s -> new RawValue(s.toString()))
					.collect(Collectors.toList()));
		}else if(setting.getType().equals(Complex.list(Boolean.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), "%s.split(\",\").map(x=>x.trim()).map(x=>x==\"true\")")));
			el = in;
			defaultValue = new ArrayValue(((List<?>) setting.getDefaultValue()).stream()
					.map(s -> new RawValue(s.toString()))
					.collect(Collectors.toList()));
		}

		if(el == null || defaultValue == null) return;
		if(!oneLineLayout) el.addLayoutOptions(DefaultLayoutOption.NEW_LINE);
		
		WebinterfaceText t = new WebinterfaceText(setting.getFriendlyName() != null ? setting.getFriendlyName() : setting.getKey());
		t.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
		if(!oneLineLayout) t.addLayoutOptions(DefaultLayoutOption.NEW_LINE);
		
		WebinterfacePageElement tEl = t;
		if(setting.getDescription() != null) {
			WebinterfaceElementGroup tGrp = new WebinterfaceElementGroup();
			tGrp.addLayoutOptions(new GridLayout("1fr"));
			tGrp.addElement(t);
			WebinterfaceText tDesc = new WebinterfaceText(setting.getDescription());
			tDesc.getStyle().setProperty("font-size", "0.8em");
			tDesc.getStyle().setProperty("color", "var(--theme-color-content-text-secondary)");
			tDesc.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
			tGrp.addElement(tDesc);
			tEl = tGrp;
		}
		
		WebinterfaceButton reset = new WebinterfaceButton("X");
		reset.setOnClickAction(changeSettingAction(setting, defaultValue));
		
		WebinterfaceElementGroup grp = new WebinterfaceElementGroup();
		grp.getStyle().setProperty("grid-template-columns", oneLineLayout ? "50px auto" : "auto 50px");
		grp.getMobileStyle().setProperty("grid-template-columns", oneLineLayout ? "50px auto" : "1fr");
		
		if(oneLineLayout) {
			// No reset button, since this currently only exists for boolean settings
			grp.addElement(el);
			grp.addElement(tEl);
		}else {
			grp.addElement(tEl);
			grp.addElement(el);
			grp.addElement(reset);
		}
		
		addElement(grp);
	}
	
	private WebinterfaceAction changeSettingAction(WebinterfaceSetting<?> setting, WebinterfaceActionValue value) {
		return new SendJSAction(requestTarget, requestMethod, new ArrayValue(
				new StringValue(setting.getKey()),
				value
			)).onSuccess(new ReloadPageAction(false, 100));
	}
	
	public List<SettingsCategory> getSettingsCategories() {
		return categories;
	}
	
	/**
	 * @deprecated Use {@link WebinterfaceSettingsPage#handleSetSettingRequest(WebinterfaceConfig, WebinterfaceRequestEvent)} instead
	 * @param config
	 * @param event
	 * @return
	 */
	@Deprecated
	public static WebinterfaceResponse handleSetSettingRequest(WebinterfaceConfig config, WebinterfaceRequestEvent event) {
		JSONArray keyAndValue = event.getRequestData().getJSONArray("value");
		WebinterfaceSetting<?> set = config.getSetting(keyAndValue.getString(0));
		WebinterfaceSettingsPane.setSetting(config, set, keyAndValue.get(1));
		return WebinterfaceResponse.success();
	}
	
	@Deprecated
	private static <T> void setSetting(WebinterfaceConfig config, WebinterfaceSetting<T> setting, Object value) {
		config.setSetting(setting, setting.getType().cast(value, WebinterfaceSettingsPane::jsonCast).get());
	}
	
	@Deprecated
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
